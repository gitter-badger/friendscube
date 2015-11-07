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

import com.dotweblabs.friendscube.app.client.shared.entity.Profile;
import com.dotweblabs.friendscube.app.client.shared.entity.User;
import com.dotweblabs.friendscube.rest.guice.SelfInjectingServerResource;
import com.dotweblabs.friendscube.rest.resources.UserProfileResource;
import com.dotweblabs.friendscube.rest.services.ProfileService;
import com.dotweblabs.friendscube.rest.services.UserService;
import com.dotweblabs.friendscube.rest.services.WebTokenService;
import com.google.inject.Inject;
import org.restlet.data.Status;

import java.util.Date;

import static com.hunchee.twist.ObjectStoreService.store;
/**
 * @author <a href="mailto:kerbymart@gmail.com">Kerby Martino</a>
 * @version 1.0
 * @since 1.0
 */
public class GaeUserProfileServerResource extends SelfInjectingServerResource
    implements UserProfileResource {

    @Inject
    ProfileService profileService;

    @Inject
    WebTokenService webTokenService;

    @Inject
    UserService userService;

    String clientToken;

    String username;

    @Override
    protected void doInit() {
        super.doInit();
        clientToken = getQueryValue("client_token");
        username = getQueryValue("username");
    }

    @Override
    public Profile getProfile() {
        Profile profile = null;
        try {
            User user = userService.read(username);
            Long userId = user == null? webTokenService.readUserIdFromToken(clientToken) : user.getId();
            if(userId != null){
                profile = profileService.readByUserId(userId);
                setStatus(Status.SUCCESS_OK);
            } else {
                setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
            }
        } catch (NumberFormatException e){
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
        }
        return profile;
    }

    @Override
    public Profile updateProfile(Profile profile) {
        getLogger().info("token: " + profile.getPhoneNumber());
        Long userId = webTokenService.readUserIdFromToken(clientToken);
        if(profile != null){
            profileService.update(profile, userId);
            return profile;
        }
        return null;
    }

    @Override
    public Profile createProfile(Profile profile) {
        Long userId = webTokenService.readUserIdFromToken(clientToken);
        if(userId != null){
            profile.setCreated(new Date());
            profile.setModified(new Date());
            store().put(profile);
            return profile;
        } else {
            setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
        }
        return null;
    }

    @Override
    public void deleteProfile() {
        if(!isAdmin()){
            setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
            return;
        }
    }

    public boolean isAdmin(){
        return false;
    }
}
