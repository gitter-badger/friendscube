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

import com.dotweblabs.friendscube.rest.guice.SelfInjectingServerResource;
import com.dotweblabs.friendscube.rest.resources.StatusResource;
import com.dotweblabs.friendscube.app.client.shared.entity.actions.Status;
import com.dotweblabs.friendscube.rest.services.StatusService;
import com.dotweblabs.friendscube.rest.services.WebTokenService;
import com.dotweblabs.friendscube.rest.utils.Base64;
import com.google.inject.Inject;
import org.restlet.util.Series;

import java.util.Date;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:kerbymart@gmail.com">Kerby Martino</a>
 * @author <a href="mailto:loucar.mendoza@gmail.com">Kerby Martino</a>
 * @version 1.0
 * @since 1.0
 */
public class GaeStatusServerResource extends
        SelfInjectingServerResource implements StatusResource {

    private static final Logger LOG = Logger.getLogger(GaeStatusServerResource.class.getName());


    @Inject
    StatusService statusService;

    @Inject
    WebTokenService webTokenService;

    String clientToken;
    String statusId;
    Long userId;

    @Override
    protected void doInit() {
        super.doInit();
        Series headers = (Series) getRequestAttributes().get("org.restlet.http.headers");
        String authValue = headers.getFirstValue("Authorization").replaceFirst("Basic ","");
        clientToken = Base64.decode(authValue).replaceFirst("Authorization:","");
        statusId = (String) getRequest().getAttributes().get("status_id");
    }

    @Override
    public Status create(Status status) {
        if(validateClientToken(clientToken)){
            status.setUserId(userId);
            status.setCreated(new Date());
            status.setModified(new Date());
            statusService.create(status);
            return status;
        } else {
            setStatus(org.restlet.data.Status.CLIENT_ERROR_BAD_REQUEST, "Invalid client token");
        }
        return null;
    }

    @Override
    public Status read() {
        if(validateClientToken(clientToken)){
            Status status =  statusService.read(statusId(statusId));
            if(status == null){
                setStatus(org.restlet.data.Status.CLIENT_ERROR_NOT_FOUND, "Status with id " + statusId + " does not exist");
            }
            return status;
        } else {
            setStatus(org.restlet.data.Status.CLIENT_ERROR_BAD_REQUEST, "Invalid client token");
        }
        return null;
    }

    @Override
    public Status update(Status status) {
        if(validateClientToken(clientToken)){
            Status saved = statusService.read(status.getId());
            saved.setModified(new Date());
            saved.setMessage(status.getMessage());
            saved.setAttachment(status.getAttachment());
            statusService.update(saved);
            return saved;
        } else {
            setStatus(org.restlet.data.Status.CLIENT_ERROR_BAD_REQUEST, "Invalid client token");
        }
        return null;
    }

    @Override
    public void remove() {
        if(validateClientToken(clientToken)){
            statusService.delete(statusId(statusId));
        } else {
            setStatus(org.restlet.data.Status.CLIENT_ERROR_BAD_REQUEST, "Invalid client token");
        }
    }

    public Long statusId(String statusId){
        Long result =  Long.valueOf(statusId);
        return result;
    }

    public boolean validateClientToken(String clientToken){
        if(clientToken != null && !clientToken.isEmpty()){
            Long userId = webTokenService.readUserIdFromToken(clientToken);
            this.userId = userId;
            LOG.info("Client token=" + clientToken);
            LOG.info("User id=" + this.userId);
            return userId != null;
        } else {
            return false;
        }
    }

}