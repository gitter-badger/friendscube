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

import com.dotweblabs.friendscube.app.client.shared.entity.actions.StatusList;
import com.dotweblabs.friendscube.rest.guice.SelfInjectingServerResource;

import com.dotweblabs.friendscube.rest.resources.StatusesResource;
import com.dotweblabs.friendscube.rest.services.StatusService;
import com.dotweblabs.friendscube.rest.services.WebTokenService;
import com.dotweblabs.friendscube.rest.utils.Base64;
import com.google.inject.Inject;
import org.restlet.util.Series;

import java.util.logging.Logger;
/**
 * @author <a href="mailto:kerbymart@gmail.com">Kerby Martino</a>
 * @version 1.0
 * @since 1.0
 */
public class GaeStatusesServerResource extends SelfInjectingServerResource
    implements StatusesResource {

    private static final Logger LOG = Logger.getLogger(GaeStatusesServerResource.class.getName());
    private static final int FETCH_LIMIT = 10;

    @Inject
    StatusService statusService;

    @Inject
    WebTokenService webTokenService;

    String clientToken;
    String cursor;
    Long userId;

    @Override
    protected void doInit() {
        super.doInit();
        cursor = getQueryValue("cursor");
        Series headers = (Series) getRequestAttributes().get("org.restlet.http.headers");
        String authValue = headers.getFirstValue("Authorization").replaceFirst("Basic ","");
        clientToken = Base64.decode(authValue).replaceFirst("Authorization:","");
    }

    @Override
    public StatusList listStatus() {
        if(validateClientToken(clientToken)){
            if(cursor == null){
                cursor = "";
            }
            StatusList list = statusService.listStatus(userId, cursor, FETCH_LIMIT);
            return list;
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
