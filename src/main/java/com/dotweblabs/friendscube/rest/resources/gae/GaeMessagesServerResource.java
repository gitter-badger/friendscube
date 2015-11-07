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

import com.dotweblabs.friendscube.app.client.shared.entity.actions.Message;
import com.dotweblabs.friendscube.app.client.shared.entity.actions.MessagesList;
import com.dotweblabs.friendscube.rest.guice.SelfInjectingServerResource;
import com.dotweblabs.friendscube.rest.resources.MessagesResource;
import com.dotweblabs.friendscube.rest.services.MessageService;
import com.dotweblabs.friendscube.rest.services.WebTokenService;
import com.google.inject.Inject;
import org.restlet.util.Series;

import java.util.logging.Logger;

/**
 * @author <a href="mailto:kerbymart@gmail.com">Kerby Martino</a>
 * @version 1.0
 * @since 1.0
 */
public class GaeMessagesServerResource extends SelfInjectingServerResource
    implements MessagesResource {

    private static final Logger LOG = Logger.getLogger(GaeMessagesServerResource.class.getName());
    private static final int FETCH_LIMIT = 10;

    @Inject
    MessageService messageService;

    @Inject
    WebTokenService webTokenService;

    String clientToken;
    Long userId;
    String cursor;
    String to;

    @Override
    protected void doInit() {
        super.doInit();
        Series headers = (Series) getRequestAttributes().get("org.restlet.http.headers");
        if(headers != null){
            String authorization = headers.getFirstValue("X-Auth-Token");
            if(authorization != null){
                clientToken = authorization;
            }
        }
        cursor = getQueryValue("cursor");
        to = getQueryValue("to");
    }

    @Override
    public MessagesList listMessage() {
        if(validateClientToken(clientToken)){
            if(cursor == null){
                cursor = "";
            }
            try{
                Long friendId = Long.valueOf(to);
                MessagesList messagesList = messageService.listMessage(userId, friendId, cursor, FETCH_LIMIT);
                return messagesList;
            } catch (Exception e) {
                setStatus(org.restlet.data.Status.CLIENT_ERROR_BAD_REQUEST, "Receiver ID must not be null");
            }
            return null;
        } else {
            setStatus(org.restlet.data.Status.CLIENT_ERROR_BAD_REQUEST, "Invalid client token");
        }
        return null;
    }

    @Override
    public Message createMessage(Message message) {
        if(validateClientToken(clientToken)){
            message.setFrom(userId);
            messageService.create(message);
            return message;
        } else {
            setStatus(org.restlet.data.Status.CLIENT_ERROR_BAD_REQUEST, "Invalid client token");
        }
        return null;
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
