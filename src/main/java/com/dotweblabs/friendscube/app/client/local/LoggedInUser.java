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
package com.dotweblabs.friendscube.app.client.local;

import com.dotweblabs.friendscube.app.client.local.storage.FriendsStorageKeyProvider;
import com.dotweblabs.friendscube.app.client.shared.entity.User;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.SerializationException;
import com.seanchenxi.gwt.storage.client.StorageExt;
import org.jboss.errai.ui.nav.client.local.TransitionTo;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
/**
 * @author <a href="mailto:kerbymart@gmail.com">Kerby Martino</a>
 * @version 1.0
 * @since 1.0
 */
@ApplicationScoped
public class LoggedInUser {

    @Inject
    TransitionTo<LoginPage> loginPage;

    private User user;

    private final FriendsStorageKeyProvider friendsStorageKeyProvider = GWT.create(FriendsStorageKeyProvider.class);

    private StorageExt storageExt;

    public User getUser() {
        if(user == null){
            try {
                user = storageExt.get(friendsStorageKeyProvider.getUserKey());
                //Logger.consoleLog("retrieved user in cache");
            } catch (SerializationException e) {
                //Logger.consoleLog(e.getMessage());
            }
        }
        return user;
    }

    @PostConstruct
    public void initStorage(){
        storageExt = StorageExt.getLocalStorage();
        if(storageExt == null) {
            storageExt = StorageExt.getSessionStorage();
        }
    }

    public void setUser(User user) {
        try {
            storageExt.put(friendsStorageKeyProvider.getUserKey(), user);
            //Logger.consoleLog("saved user in cache");
        } catch (SerializationException e) {
            //Logger.consoleLog(e.getMessage());
        }
        this.user = user;
    }

    public void clear(){
        clearStorages();
        this.user = null;
    }

    private void clearStorages() {
        if(StorageExt.getLocalStorage() == null){
            StorageExt.getSessionStorage().clear();
            StorageExt.getSessionStorage().clearCache();
            //Logger.consoleLog("cleared session storage");
        } else {
            StorageExt.getLocalStorage().clear();
            StorageExt.getLocalStorage().clearCache();
            //Logger.consoleLog("cleared local storage");
        }

    }

}
