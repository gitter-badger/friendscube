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

import com.dotweblabs.friendscube.app.client.shared.entity.Registration;
import com.dotweblabs.friendscube.rest.services.RegistrationService;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.hunchee.twist.types.ListResult;

import java.util.Date;
import java.util.List;

import static com.hunchee.twist.ObjectStoreService.store;

/**
 * @author <a href="mailto:kerbymart@gmail.com">Kerby Martino</a>
 * @version 1.0
 * @since 1.0
 */
public class GaeRegistrationService implements RegistrationService {
    @Override
    public Key create(Registration registration) {
        registration.setCreated(new Date());
        registration.setModified(new Date());
        return store().put(registration);
    }

    @Override
    public Registration read(Long id) {
        return store().get(Registration.class, id);
    }

    @Override
    public Registration read(Key key) {
        return store().get(Registration.class, key);
    }

    @Override
    public Registration read(String email) {
        Registration registration = store().get(Registration.class, email);
        if(registration != null){
            Key key = store().put(registration);
            registration.setToken(KeyFactory.keyToString(key));
        }
        return registration;
    }

    @Override
    public Key update(Registration registration) {
        registration.setModified(new Date());
        return store().put(registration);
    }

    @Override
    public void delete(String email) {
        store().delete(Registration.class, email);
    }

    @Override
    public List<Registration> list() {
        ListResult<Registration> result = store().find(Registration.class)
                .limit(100)
                .sortDescending("created")
                .asList();
        return result.getList();
    }

    @Override
    public String readToken(String email) {
        Registration registration = read(email);
        if(registration != null){
            return registration.getToken();
        }
        return null;
    }
}
