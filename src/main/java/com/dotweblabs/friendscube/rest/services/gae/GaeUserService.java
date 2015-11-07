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
package com.dotweblabs.friendscube.rest.services.gae;

import com.dotweblabs.friendscube.app.client.shared.entity.User;
import com.dotweblabs.friendscube.rest.services.UserService;
import java.util.NoSuchElementException;

import static com.hunchee.twist.ObjectStoreService.store;
/**
 * @author <a href="mailto:kerbymart@gmail.com">Kerby Martino</a>
 * @version 1.0
 * @since 1.0
 */
public class GaeUserService implements UserService {

    @Override
    public void create(User user) {
        store().put(user);
    }

    @Override
    public User read(Long id) {
        return store().get(User.class, id);
    }

    @Override
    public User read(String username) {
        try {
            User user = (User) store().find(User.class)
                    .equal("username", username).now().next();
            return user;
        } catch (NoSuchElementException e){
            //e.printStackTrace();
            // do nothing just return null
        }
        return null;
    }

    @Override
    public void update(User user) {
        User storedUser = (User) store().get(User.class,user.getId());
        storedUser.setPasswordHash(user.getPasswordHash());
        storedUser.setProfile(user.getProfile());
        store().put(storedUser);
    }

    @Override
    public void delete(Long id) {

    }
}
