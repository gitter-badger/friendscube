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

import com.dotweblabs.friendscube.app.client.local.util.ClientProxyHelper;
import com.dotweblabs.friendscube.app.client.local.widgets.connections.FriendsPageItem;
import com.dotweblabs.friendscube.app.client.shared.UserResourceProxy;
import com.dotweblabs.friendscube.app.client.shared.entity.User;
import com.dotweblabs.friendscube.app.client.shared.entity.relationships.Friend;
import com.dotweblabs.friendscube.app.client.shared.entity.relationships.Friends;
import com.dotweblabs.friendscube.app.client.local.widgets.UserNavPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.dotweblabs.friendscube.app.client.local.widgets.NavBar;
import com.google.gwt.user.client.ui.FlowPanel;
import org.jboss.errai.ui.nav.client.local.Page;
import org.jboss.errai.ui.shared.ServerTemplateProvider;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.restlet.client.resource.Result;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
/**
 * @author <a href="mailto:kerbymart@gmail.com">Kerby Martino</a>
 * @version 1.0
 * @since 1.0
 */
@Dependent
@Templated(value = "/FriendsPage.html#friends", provider = ServerTemplateProvider.class)
@Page
public class FriendsPage extends Composite {

    @Inject
    @DataField
    NavBar navBar;

    @Inject
    @DataField
    UserNavPanel userNav;

    @Inject
    LoggedInUser loggedInUser;

    @Inject
    Instance<FriendsPageItem> friendsPageItem;

    @Inject
    @DataField
    FlowPanel friendsList;



    @PostConstruct
    public void buildUI(){
        UserResourceProxy userResourceProxy = GWT.create(UserResourceProxy.class);
        Long userId = loggedInUser.getUser().getId();
        userResourceProxy.getClientResource().setReference(ClientProxyHelper.restRootPath() + UserResourceProxy.USERS_URI+"/"+userId+"/friends");
        userResourceProxy.listFriends(userId, new Result<Friends>() {
            @Override
            public void onFailure(Throwable throwable) {

            }
            @Override
            public void onSuccess(Friends friends) {
                for(Friend friend : friends.getFriends()){
                    getFriendInfo(friend);
                }
            }
        });
    }

    private void getFriendInfo(Friend friend) {
        UserResourceProxy userResourceProxy = GWT.create(UserResourceProxy.class);
        userResourceProxy.getClientResource().setReference(ClientProxyHelper.restRootPath() + UserResourceProxy.USERS_URI + "/" + friend.getFriendId());
        userResourceProxy.retrieve(new Result<User>() {
            @Override
            public void onFailure(Throwable throwable) {

            }
            @Override
            public void onSuccess(User user) {
                FriendsPageItem pageItem = friendsPageItem.get();
                pageItem.setProfile(user.getProfile());
                friendsList.add(pageItem);
            }
        });

    }

}
