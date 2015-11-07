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
package com.dotweblabs.friendscube.app.client.local.widgets;

import com.dotweblabs.friendscube.app.client.local.LoggedInUser;
import com.dotweblabs.friendscube.app.client.local.util.ClientProxyHelper;
import com.dotweblabs.friendscube.app.client.local.util.CssHelper;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.user.client.ui.*;
import org.jboss.errai.ui.nav.client.local.PageShown;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

/**
 * @author <a href="mailto:kerbymart@gmail.com">Kerby Martino</a>
 * @version 1.0
 * @since 1.0
 */
@Dependent
@Templated
public class UploadFileModal extends Composite {


    public static final String FILES_ENDPOINT = ClientProxyHelper.restRootPath() + "files";
    @Inject
    LoggedInUser loggedInUser;

    Image imageElement;

    String uploadType;

    public void setImageElement(Image imageElement) {
        this.imageElement = imageElement;
    }

    public void setUploadType(String uploadType) {
        this.uploadType = uploadType;
    }


    @PostConstruct
    public void buildUI(){
        StyleInjector.inject(".uk-modal-dialog {top : 100px}");
        CssHelper.loadCss("fine-uploader/fine-uploader.css");
        CssHelper.loadCss("fine-uploader/friendscube-uploader.css");
        ScriptInjector.fromUrl("fine-uploader/fine-uploader.js").setWindow(ScriptInjector.TOP_WINDOW).inject();
    }

    @PageShown
    public void ready(){


    }

    public void show(){
        loadUploadForm(this.getElement(), FILES_ENDPOINT, loggedInUser.getUser().getClientToken(), uploadType);
        showModal(this.getElement());
    }

    private void handleSuccess(String url){
        if(imageElement != null) {
            imageElement.getElement().setAttribute("src", url);
        }

        if("profile_pic".equals(uploadType)){
            loggedInUser.getUser().getProfile().setPhoto(url);
        }else if("cover_photo".equals(uploadType)){
            loggedInUser.getUser().getProfile().setCoverPhoto(url);
        }
    }

    private native void loadUploadForm(Element el, String filesEndpoint, String clientToken, String uploadType)/*-{
        var objInstance = this
        $wnd.successCallback = function(id, name, responseJSON, xhr){
            objInstance.@UploadFileModal::handleSuccess(*)(responseJSON.image_url);
        }
        var manualUploader = new $wnd.qq.FineUploader({
            element: $doc.getElementById('friendscube-fine-uploader'),
            template: 'qq-simple-thumbnails-template',
            request: {
                endpoint: filesEndpoint,
                paramsInBody : false,
                params :{
                    client_token: clientToken,
                    upload_type: uploadType
                }
            },
			cors : {
				expected : true,
				sendCredentials : true

			},
			validation : {
				acceptFiles : ['.jpg','.png']
			},
            thumbnails: {
                placeholders: {
                    waitingPath: 'fine-uploader/placeholders/waiting-generic.png',
                    notAvailablePath: 'fine-uploader/placeholders/not_available-generic.png'
                }
            },
            callbacks : {
				onComplete : $wnd.successCallback
			},
            debug: true
        });

    }-*/;

    private static native void showModal(Element el)/*-{
        var modal = $wnd.UIkit.modal(el);
        if ( modal.isActive() ) {
            modal.hide();
        } else {
            modal.show();
        }
    }-*/;

}
