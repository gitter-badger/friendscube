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
import com.dotweblabs.friendscube.app.client.local.widgets.*;
import com.dotweblabs.friendscube.app.client.local.widgets.connections.ConnectionsWidget;
import com.dotweblabs.friendscube.app.client.shared.ProfileResourceProxy;
import com.dotweblabs.friendscube.app.client.shared.StatusResourceProxy;
import com.dotweblabs.friendscube.app.client.shared.StatusesResourceProxy;
import com.dotweblabs.friendscube.app.client.shared.entity.Profile;
import com.dotweblabs.friendscube.app.client.shared.entity.actions.StatusList;
import com.dotweblabs.friendscube.app.client.shared.entity.User;
import com.dotweblabs.friendscube.app.client.shared.entity.actions.Status;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import org.jboss.errai.ui.nav.client.local.*;
import org.jboss.errai.ui.shared.ServerTemplateProvider;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.restlet.client.data.ChallengeScheme;
import org.restlet.client.resource.Result;

/**
 * @author <a href="mailto:kerbymart@gmail.com">Kerby Martino</a>
 * @version 1.0
 * @since 1.0
 */
@Dependent
@Templated(value = "/WelcomePage.html#welcome", provider = ServerTemplateProvider.class)
@Page(role = DefaultPage.class)
public class WelcomePage extends Composite {

    @Inject
    @DataField
    Anchor uploadProfilePic;

//    @Inject
//    @DataField
//    Anchor uploadCoverPhoto;

    @Inject
    @DataField
    UserInfoWidget userInfo;

    @Inject
    TransitionTo<LoginPage> loginPage;

    @Inject
    TransitionTo<MessagesPage> messagesPage;

    @Inject
    TransitionTo<SearchPage> searchPage;

    @Inject
    TransitionTo<UserFeedsPage> userFeeds;

    @Inject
    @DataField
    FlowPanel statuses;

    @Inject
    @DataField
    Button statusButton;

    @Inject
    @DataField
    Label statusLabel;

    @Inject
    @DataField
    TextArea statusText;

    @Inject
    @DataField
    Image profilePic;

    @Inject
    @DataField
    NavBar navBar;

    @Inject
    Instance<StatusWidget> statusInstance;

    @Inject
    @DataField
    UploadFileModal uploadProfilePicModal;

    @Inject
    @DataField
    ConnectionsWidget userConnections;

    @Inject
    LoggedInUser loggedInUser;

    @PageState
    String token;

    @PageShowing
    public void showing() {
    }

    @PageShown
    public void ready() {
        //Logger.consoleLog("user: " + loggedInUser.getUser());
        if (loggedInUser.getUser() == null) {
            //Logger.consoleLog("going to login");
            loginPage.go();
        } else {
            User user = loggedInUser.getUser();
            userInfo.setModel(loggedInUser.getUser());

            ProfileResourceProxy profileResourceProxy = GWT.create(ProfileResourceProxy.class);
            profileResourceProxy.getClientResource().setReference(ClientProxyHelper.restRootPath() + ProfileResourceProxy.PROFILE_URI);
            profileResourceProxy.getClientResource().addQueryParameter("client_token", loggedInUser.getUser().getClientToken());

            profileResourceProxy.retrieve(new Result<Profile>() {
                @Override
                public void onFailure(Throwable throwable) {
                    Window.alert("Could not retrieve profile");
                }

                @Override
                public void onSuccess(Profile result) {
                    loggedInUser.getUser().setProfile(result);
                    statusLabel.setText("What's up " + result.getFirstName());
                    if(result.getPhoto() != null && !result.getPhoto().isEmpty()){
                        profilePic.getElement().setAttribute("src", result.getPhoto());
                    }
                }
            });

            StatusesResourceProxy statusesResourceProxy = GWT.create(StatusesResourceProxy.class);
            statusesResourceProxy.getClientResource().setReference(ClientProxyHelper.restRootPath() + StatusesResourceProxy.STATUSES_URI);
            statusesResourceProxy.getClientResource().setChallengeResponse(ChallengeScheme.HTTP_BASIC, "Authorization", loggedInUser.getUser().getClientToken());
            statusesResourceProxy.retrieve(new Result<StatusList>() {
                @Override
                public void onFailure(Throwable throwable) {
                    Window.alert("Cannot retrieve statuses");
                }

                @Override
                public void onSuccess(StatusList statusList) {
                    for (Status status : statusList.getStatuses()) {
                        StatusWidget w = statusInstance.get();
                        w.setProfile(loggedInUser.getUser().getProfile());
                        w.setModel(status);
                        statuses.insert(w, 0);
                    }
                }
            });
//            userConnections.setVisible(true);
            userConnections.ready();
        }
    }

    @PostConstruct
    public void buildUI() {

    }

    @EventHandler("statusButton")
    public void post(ClickEvent event) {
        event.preventDefault();
        StatusResourceProxy statusResourceProxy = GWT.create(StatusResourceProxy.class);

        final Status status = new Status();
        status.setMessage(statusText.getText());

        statusResourceProxy.getClientResource().setReference(ClientProxyHelper.restRootPath() + StatusResourceProxy.STATUSES_URI + "/");
        statusResourceProxy.getClientResource().setChallengeResponse(ChallengeScheme.HTTP_BASIC, "Authorization", loggedInUser.getUser().getClientToken());
        statusResourceProxy.create(status, new Result<Status>() {
            @Override
            public void onFailure(Throwable throwable) {
                Window.alert(throwable.getMessage());
            }

            @Override
            public void onSuccess(Status result) {
                StatusWidget w = statusInstance.get();
                w.setProfile(loggedInUser.getUser().getProfile());
                w.setModel(result);
                statuses.insert(w, 0);
            }
        });

    }

    @EventHandler("uploadProfilePic")
    public void uploadProfilePic(ClickEvent event) {
        event.preventDefault();
        uploadProfilePicModal.setImageElement(profilePic);
        uploadProfilePicModal.setUploadType("profile_pic");
        uploadProfilePicModal.show();
    }

//    @EventHandler("uploadCoverPhoto")
//    public void uploadCoverPhoto(ClickEvent event) {
//        event.preventDefault();
//        uploadProfilePicModal.setUploadType("cover_photo");
//        uploadProfilePicModal.show();
//    }


}
