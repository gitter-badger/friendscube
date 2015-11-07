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
import com.dotweblabs.friendscube.rest.resources.TokensResource;
import com.dotweblabs.friendscube.rest.services.UserService;
import com.dotweblabs.friendscube.rest.services.WebTokenService;
import com.google.inject.Inject;
import org.mindrot.jbcrypt.BCrypt;
import org.restlet.data.Status;
import java.util.logging.Logger;
/**
 * @author <a href="mailto:kerbymart@gmail.com">Kerby Martino</a>
 * @version 1.0
 * @since 1.0
 */
public class GaeTokensServerResource extends SelfInjectingServerResource
    implements TokensResource {

    private static final Logger LOG
            = Logger.getLogger(GaeTokensServerResource.class.getName());


    @Inject
    UserService userService;

    @Inject
    WebTokenService webTokenService;

    String email;
    String password;

    @Override
    protected void doInit() {
        super.doInit();
        email = getQueryValue("email");
        password = getQueryValue("password");
    }

    @Override
    public User verify() {
        if(email != null
                && password != null
                && !email.isEmpty()
                && !password.isEmpty()){
            User user = userService.read(email);
            if(user != null){
                String passwordHash = user.getPasswordHash();
                LOG.info("Password: " + password);
                if(BCrypt.checkpw(password, passwordHash)){
                    user.setClientToken(webTokenService.createToken(user.getId()));
                    return user;
                } else {
                    setStatus(Status.CLIENT_ERROR_UNAUTHORIZED, "Email and/or password is invalid");
                }
            } else {
                setStatus(Status.CLIENT_ERROR_NOT_FOUND, "User with email " + email + " not found");
                return null;
            }
        } else {
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
        }
        return null;
    }
}
