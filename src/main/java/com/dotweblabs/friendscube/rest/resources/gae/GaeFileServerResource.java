/*
* Copyright 2015 Dotweblabs Web Technologies
*
*   Licensed under the Apache License, Version 2.0 (the "License");
*   you may not use this file except in compliance with the License.
*   You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
*   Unless required by applicable law or agreed to in writing, software
*   distributed under the License is distributed on an "AS IS" BASIS,
*   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*   See the License for the specific language governing permissions and
*   limitations under the License.
*/
package com.dotweblabs.friendscube.rest.resources.gae;

import com.dotweblabs.friendscube.app.client.shared.entity.User;
import com.dotweblabs.friendscube.rest.guice.SelfInjectingServerResource;
import com.dotweblabs.friendscube.rest.resources.FileResource;
import com.dotweblabs.friendscube.rest.services.ProfileService;
import com.dotweblabs.friendscube.rest.services.UserService;
import com.dotweblabs.friendscube.rest.services.WebTokenService;
import com.dotweblabs.friendscube.rest.utils.URLUtil;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.appengine.repackaged.com.google.common.io.ByteStreams;
import com.google.appengine.repackaged.org.apache.commons.fileupload.FileItemIterator;
import com.google.appengine.repackaged.org.apache.commons.fileupload.FileItemStream;
import com.google.appengine.repackaged.org.apache.commons.fileupload.servlet.ServletFileUpload;
import com.google.appengine.repackaged.org.apache.commons.fileupload.servlet.ServletRequestContext;
import com.google.appengine.tools.cloudstorage.*;
import com.google.inject.Inject;
import org.restlet.Request;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.engine.io.InputStreamChannel;
import org.restlet.ext.servlet.ServletUtils;
import org.restlet.representation.ReadableRepresentation;
import org.restlet.representation.Representation;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.util.logging.Logger;
/**
 * @author <a href="mailto:kerbymart@gmail.com">Kerby Martino</a>
 * @version 1.0
 * @since 1.0
 */
public class
        GaeFileServerResource extends SelfInjectingServerResource
    implements FileResource {

    private static final Logger LOG
            = Logger.getLogger(GaeFileServerResource.class.getName());

    private final GcsService gcsService = GcsServiceFactory.createGcsService(RetryParams.getDefaultInstance());

    private final BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

    String fileName;

    @Inject
    ProfileService profileService;

    @Inject
    UserService userService;

    @Inject
    WebTokenService webTokenService;

    @Override
    protected void doInit() {
        super.doInit();
        fileName = getQueryValue("filename");
    }

    @Override
    public String upload(Representation entity) throws Exception {
        if (entity != null) {
            if (MediaType.MULTIPART_FORM_DATA.equals(entity.getMediaType(), true)) {
                Request restletRequest = getRequest();
                HttpServletRequest servletRequest = ServletUtils.getRequest(restletRequest);
                ServletFileUpload upload = new ServletFileUpload();
                FileItemIterator fileIterator = upload.getItemIterator(servletRequest);
                getLogger().info("content type: " + servletRequest.getContentType());
                getLogger().info("content: " + new ServletRequestContext(servletRequest).getContentLength());

                getLogger().info("iterator: " + fileIterator.hasNext());
                while (fileIterator.hasNext()) {
                    FileItemStream item = fileIterator.next();

                    String name = item.getName();
                    String fileName = getQueryValue("client_token") + "_" + name; // TODO note storing client_token is dangerous
                    GcsFilename gcsFilename = new GcsFilename(getBucketName(),fileName);
                    getLogger().info("using bucket.. " + getBucketName());
                    byte[] byteContent = ByteStreams.toByteArray(item.openStream());
                    // TODO byteContent is basicallly the file uploaded

                    getLogger().info("contentType: " + item.getContentType());
                    GcsOutputChannel  outputChannel = gcsService.createOrReplace(gcsFilename, GcsFileOptions.getDefaultInstance());
                    outputChannel.write(ByteBuffer.wrap(byteContent));
                    outputChannel.close();
                    getLogger().info("uploaded");

                    BlobKey blobKey = blobstoreService.createGsBlobKey("/gs/"+gcsFilename.getBucketName()+"/"+gcsFilename.getObjectName());

                    ObjectNode result = JsonNodeFactory.instance.objectNode();
                    processFile(blobKey, getQueryValue("client_token"), getQueryValue("upload_type"), result);
                    return result.toString();
                }
            } else {
                setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            }
        } else {
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
        }
        return null;
    }

    private void processFile(BlobKey blobKey, String clientToken, String uploadType, ObjectNode result) {
        getLogger().info("token: " + clientToken);
        if("profile_pic".equals(uploadType)){
            ImagesService imageService = ImagesServiceFactory.getImagesService();
            ServingUrlOptions serve = ServingUrlOptions.Builder.withBlobKey(blobKey);
            String imageUrl = imageService.getServingUrl(serve);
            User user = userService.read(webTokenService.readUserIdFromToken(clientToken));
            user.getProfile().setPhoto(imageUrl);
            userService.update(user);
            result.put("image_url", imageUrl);
            result.put("success", true);
            getLogger().info("url: " + imageUrl);
        } else if("cover_photo".equals(uploadType)){
            ImagesService imageService = ImagesServiceFactory.getImagesService();
            ServingUrlOptions serve = ServingUrlOptions.Builder.withBlobKey(blobKey);
            String imageUrl = imageService.getServingUrl(serve);
            User user = userService.read(webTokenService.readUserIdFromToken(clientToken));
            user.getProfile().setCoverPhoto(imageUrl);
            userService.update(user);
            result.put("image_url", imageUrl);
            result.put("success", true);
            getLogger().info("url: " + imageUrl);
        }
    }

    @Override
    public ReadableRepresentation download() throws Exception {
        InputStreamChannel inputStreamChannel;
        byte[] byteContent = getFile(fileName);
        inputStreamChannel = new InputStreamChannel(new ByteArrayInputStream(byteContent));
        return new ReadableRepresentation(inputStreamChannel, MediaType.IMAGE_JPEG); // TODO: select correct type
    }

    public byte[] getFile(String fileName){
        // TODO: Get file and put bytes here
        return null;
    }

    private String getBucketName(){
        return URLUtil.getBasePath();
    }
}
