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

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import com.dotweblabs.friendscube.app.client.local.util.ClientProxyHelper;
import com.dotweblabs.friendscube.app.client.local.widgets.NavBar;
import com.dotweblabs.friendscube.app.client.local.widgets.messages.MessageFriendWidget;
import com.dotweblabs.friendscube.app.client.local.widgets.messages.MessageWidget;
import com.dotweblabs.friendscube.app.client.shared.MessageResourceProxy;
import com.dotweblabs.friendscube.app.client.shared.UserResourceProxy;
import com.dotweblabs.friendscube.app.client.shared.entity.actions.Message;
//import MessagesList;
import com.dotweblabs.friendscube.app.client.shared.entity.actions.MessagesList;
import com.dotweblabs.friendscube.app.client.shared.entity.relationships.Friend;
import com.dotweblabs.friendscube.app.client.shared.entity.relationships.Friends;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import org.jboss.errai.ui.nav.client.local.PageShowing;
import org.jboss.errai.ui.shared.ServerTemplateProvider;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.jboss.errai.ui.nav.client.local.TransitionTo;
import org.restlet.client.resource.Result;
import com.google.gwt.user.client.ui.Composite;
import org.jboss.errai.ui.nav.client.local.Page;

import java.util.List;

/**
 * @author <a href="mailto:kerbymart@gmail.com">Kerby Martino</a>
 * @version 1.0
 * @since 1.0
 */
@Dependent
@Templated(value = "/MessagesPage.html#messages", provider = ServerTemplateProvider.class)
@Page
public class MessagesPage extends Composite  {
    @Inject
    TransitionTo<WelcomePage> welcomePage;

    @Inject
    TransitionTo<LoginPage> loginPage;

    @Inject
    TransitionTo<SearchPage> searchPage;

    @Inject
    TransitionTo<UserFeedsPage> userFeeds;

    @Inject
    @DataField
    NavBar navBar;

    @Inject
    @DataField
    Button sendButton;

    @Inject
    @DataField
    TextArea message;

    @Inject
    LoggedInUser loggedInUser;

    //Profile profile;

    @Inject
    Instance<MessageWidget> messageInstance;

    @Inject
    Instance<MessageFriendWidget> messageFriendInstance;

    Long selectedFriend = null;

    @Inject
    @DataField
    FlowPanel messagesflow;

    @Inject
    @DataField
    FlowPanel messagesFriendflow;

    @PageShowing
    public void showing(){
        final UserResourceProxy userResourceProxy = GWT.create(UserResourceProxy.class);

        Long userId = loggedInUser.getUser().getId();
        userResourceProxy.getClientResource().setReference(ClientProxyHelper.restRootPath() + UserResourceProxy.USERS_URI+"/"+userId+"/friends");
        userResourceProxy.listFriends(userId, new Result<Friends>() {
            @Override
            public void onFailure(Throwable throwable) {
                Window.alert("Failed to get list of Friends");
            }

            @Override
            public void onSuccess(Friends friends) {
                if(friends == null || friends.getCount() == 0){

                }
                else {
                    List<Friend> friendList = friends.getFriends();
                    for(final Friend friend : friendList){
                        MessageFriendWidget widget = messageFriendInstance.get();
                        widget.setModel(friend);
                        FocusPanel focusPanel = new FocusPanel();
                        focusPanel.setWidget(widget);
                        focusPanel.addClickHandler(new ClickHandler() {
                            @Override
                            public void onClick(ClickEvent clickEvent) {
                                selectedFriend = friend.getFriendId();
                                //Logger.consoleLog(selectedFriend.toString());
                                MessageResourceProxy messageResource = GWT.create(MessageResourceProxy.class);
                                messageResource.getClientResource().setReference(ClientProxyHelper.restRootPath() + MessageResourceProxy.MESSAGES_URI);
                                messageResource.retrieve(new Result<MessagesList>() {
                                    @Override
                                    public void onFailure(Throwable caught) {
                                        Window.alert("Message failed.");
                                    }

                                    @Override
                                    public void onSuccess(MessagesList messagesList) {
                                        for (Message message : messagesList.getMessages()) {
                                            MessageWidget w = messageInstance.get();
                                            w.setProfile(loggedInUser.getUser().getProfile());
                                            w.setMessage(message);
                                            messagesflow.insert(w, 0);

                                        }
                                    }
                                });

                            }

                        });
                        messagesFriendflow.add(focusPanel);
                    }

//                    int cnt = friendscube.getFriends().size();
//                    for (int i = 0; i <  cnt; i++){
//                        Window.alert("loop");
//                        //Window.alert(friendscube.getFriends().get(i).getFriendId().toString());
//                        loadUser(friendscube.getFriends().get(i).getFriendId());

                }
            }
        });


    }


//    private void loadUser(final Long friendId) {
//
////        Window.alert("loading user");
//
//
//        UserResourceProxy userResourceProxy = GWT.create(UserResourceProxy.class);
//        userResourceProxy.getClientResource().setReference(ClientProxyHelper.restRootPath() + UserResourceProxy.USERS_URI + "/" + friendId);
//        userResourceProxy.retrieve(new Result<User>() {
//            @Override
//            public void onFailure(Throwable throwable) {
//                Window.alert("Cannot retrieve user..");
//            }
//
//            @Override
//            public void onSuccess(User result) {
//                //Logger.consoleLog("try1");
//                MessageFriendWidget x = messageFriendInstance.get();
//                //Logger.consoleLog("try2");
//                Window.alert(result.getId().toString());
//                x.setProfile(result.getProfile());
//                x.setMessage(result);
//
//                messagesFriendflow.add(x);
//            }
//        });
//    }


    @PostConstruct
    public void buildUI(){
    }

    @EventHandler("sendButton")
    public void setMsg(ClickEvent event){
       // Window.alert("a");
        event.preventDefault();
        String Msg = message.getText();

//        MessageResourceProxy messageResource = GWT.create(MessageResourceProxy.class);
//
//        messageResource.getClientResource().setReference(ClientProxyHelper.restRootPath() + MessageResourceProxy.MESSAGES_URI);
//        messageResource.getClientResource().setMethod(Method.POST);
//
//        Message message = new Message(loggedInUser.getUser().getId(),selectedFriend,Msg,null,null);
//        message.setMessageType(Message.MessageType.NEW.toString());
//        messageResource.store(message, new Result<Message>() {
//            @Override
//            public void onFailure(Throwable caught) {
//                Window.alert("Message failed.");
//            }
//
//            @Override
//            public void onSuccess(Message result) {
//
//
//                MessageWidget w = messageInstance.get();
//
//                w.setProfile(loggedInUser.getUser().getProfile());
//                w.setMessage(result);
//
//                messagesflow.add(w);
//            }
//        });

    }
}


