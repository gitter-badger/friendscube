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
package com.dotweblabs.friendscube.app.client.local.widgets.connections;

import com.dotweblabs.friendscube.app.client.local.LoggedInUser;
import com.dotweblabs.friendscube.app.client.local.util.ClientProxyHelper;
import com.dotweblabs.friendscube.app.client.shared.UserResourceProxy;
import com.dotweblabs.friendscube.app.client.shared.entity.User;
import com.dotweblabs.friendscube.app.client.shared.entity.relationships.Friends;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.*;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import org.jboss.errai.ui.nav.client.local.PageShown;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.restlet.client.resource.Result;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

/**
 * @author <a href="mailto:kerbymart@gmail.com">Kerby Martino</a>
 * @version 1.0
 * @since 1.0
 */
@Dependent
@Templated
public class ConnectionsWidget extends Composite{

    @Inject
    @DataField
    FlowPanel connectionsPlaceHolder;

    @Inject
    LoggedInUser loggedInUser;

    @Inject
    Instance<ConnectionsRowWidget> connectionsRowWidget;


    @Inject
    Instance<ConnectionsThumbnailWidget> connectionsThumbnailWidget;

    @PageShown
    public void ready(){
        UserResourceProxy userResourceProxy = GWT.create(UserResourceProxy.class);
        Long userId = loggedInUser.getUser().getId();
        userResourceProxy.getClientResource().setReference(ClientProxyHelper.restRootPath() + UserResourceProxy.USERS_URI+"/"+userId+"/friendscube");
        userResourceProxy.listFriends(userId, new Result<Friends>() {
            @Override
            public void onFailure(Throwable throwable) {
                Window.alert("Failed to get list of Friends");
            }

            @Override
            public void onSuccess(Friends friends) {
                if(friends == null || friends.getCount() == 0){
                    ButtonElement inviteButton = ButtonElement.as(new HTML("<button class=\"uk-button uk-width-small-5-10\" >Invite Users</button>").getElement());
                    DivElement inviteDiv = DivElement.as(new HTML("<div class=\"uk-form-row\"></div>").getElement());
                    inviteDiv.appendChild(inviteButton);
                    connectionsPlaceHolder.getElement().appendChild(inviteDiv);
                }   else {
                    int cnt = friends.getFriends().size();
                    ConnectionsRowWidget row = connectionsRowWidget.get();
                    connectionsPlaceHolder.add(row);
                    for (int i = 0; i <  cnt; i++){
//                        Window.alert("loop");
                        if(i % 4 == 0 && i > 0){
                            row = connectionsRowWidget.get();
                            connectionsPlaceHolder.add(row);
                        }
                        loadUser(friends.getFriends().get(i).getFriendId(), row);
                    }
                }
            }
        });
//        show(this.getElement());
    }

    private void loadUser(Long friendId, final ConnectionsRowWidget row) {
//        Window.alert("loading user");
        UserResourceProxy userResourceProxy = GWT.create(UserResourceProxy.class);
        userResourceProxy.getClientResource().setReference(ClientProxyHelper.restRootPath() + UserResourceProxy.USERS_URI + "/" + friendId);
        userResourceProxy.retrieve(new Result<User>() {
            @Override
            public void onFailure(Throwable throwable) {
                Window.alert("Cannot retrieve user..");
            }

            @Override
            public void onSuccess(User user) {
                ConnectionsThumbnailWidget thumbnail = connectionsThumbnailWidget.get();
                thumbnail.setUser(user);
                row.getConnectionRowContainer().add(thumbnail);
            }
        });
    }

    private native void show(Element el)/*-{
       el.show();
    }-*/;

}
