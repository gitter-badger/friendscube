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

import com.dotweblabs.friendscube.app.client.shared.entity.Profile;
import com.dotweblabs.friendscube.app.client.shared.entity.User;
import com.dotweblabs.friendscube.app.client.shared.entity.search.ProfileSearchItem;
import com.dotweblabs.friendscube.app.client.shared.entity.search.SearchItems;
import com.dotweblabs.friendscube.rest.services.ProfileService;
import com.dotweblabs.friendscube.rest.services.UserService;
import com.dotweblabs.friendscube.rest.services.WebTokenService;
import com.google.appengine.api.datastore.Key;
import com.google.inject.Inject;
import com.hunchee.twist.types.Function;
import com.hunchee.twist.types.ListResult;

import java.util.Date;

import static com.hunchee.twist.ObjectStoreService.store;

/**
 * @author <a href="mailto:kerbymart@gmail.com">Kerby Martino</a>
 * @author <a href="mailto:loucar.mendoza@gmail.com">Loucar Mendoza</a>
 * @version 1.0
 * @since 1.0
 */
public class GaeProfileService implements ProfileService {

    @Override
    public void create(final Profile profile) {
        store().transact(new Function<Key>() {
            @Override
            public Key execute() {
                User user = profile.getUser();
                Key profileKey = store().put(user);
              return profileKey;
            }
        });
    }

    @Inject
    UserService userService;

    @Inject
    WebTokenService webTokenService;

    @Override
    public Profile read(Long id) {
        return store().get(Profile.class, id);
    }

    @Override
    public Profile readByUserId(Long userId) {
        User user  = userService.read(userId);
        if(user != null){
            Profile profile = user.getProfile();
            profile.setUser(user);
            return profile;
        }
        return null;
    }

    @Override
    public boolean update(Profile profile, Long userId) {
        User user = userService.read(userId);
        Profile storedProfile = user.getProfile();
        storedProfile.setFirstName(profile.getFirstName());
        storedProfile.setMiddleName(profile.getMiddleName());
        storedProfile.setLastName(profile.getLastName());
        storedProfile.setPhoneNumber(profile.getPhoneNumber());
        storedProfile.setAboutMe(profile.getAboutMe());
        storedProfile.setJobTitle(profile.getJobTitle());
        storedProfile.setEmail(profile.getEmail());
        storedProfile.setModified(new Date());
        user.setProfile(storedProfile);
        userService.update(user);
        return true;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    @Override
    public SearchItems findProfiles(String query) {
        ListResult<User> userList = store().find(User.class)
                .withCursor("")
                .equal("username", query)
                .limit(3)
                .asList();

        System.out.println("query: " + userList.getList().size());
        SearchItems searchItems = new SearchItems();
        for (User u : userList.getList()){
            Profile p = u.getProfile();
            u.setClientToken(webTokenService.createToken(u.getId()));
            p.setUser(u);
            searchItems.getSearchItems().add(new ProfileSearchItem(p));
        }
        System.out.println("profiles found: " + userList.getList().size());
        return searchItems;
    }
}


