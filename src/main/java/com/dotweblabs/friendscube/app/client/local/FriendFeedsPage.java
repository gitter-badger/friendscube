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
import com.dotweblabs.friendscube.app.client.local.widgets.NavBar;
import com.dotweblabs.friendscube.app.client.local.widgets.StatusWidget;
import com.dotweblabs.friendscube.app.client.local.widgets.UserNavPanel;
import com.dotweblabs.friendscube.app.client.shared.ActivityResourceProxy;
import com.dotweblabs.friendscube.app.client.shared.ProfileResourceProxy;
import com.dotweblabs.friendscube.app.client.shared.StatusesResourceProxy;
import com.dotweblabs.friendscube.app.client.shared.entity.Profile;
import com.dotweblabs.friendscube.app.client.shared.entity.User;
import com.dotweblabs.friendscube.app.client.shared.entity.actions.Status;
import com.dotweblabs.friendscube.app.client.shared.entity.actions.StatusList;
import com.dotweblabs.friendscube.app.client.shared.entity.activities.Activity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import org.jboss.errai.ui.client.widget.HasModel;
import org.jboss.errai.ui.nav.client.local.Page;
import org.jboss.errai.ui.nav.client.local.PageShown;
import org.jboss.errai.ui.nav.client.local.PageState;
import org.jboss.errai.ui.nav.client.local.TransitionTo;
import org.jboss.errai.ui.shared.ServerTemplateProvider;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.restlet.client.data.ChallengeScheme;
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
@Templated(value = "/FriendFeedsPage.html#friendfeeds", provider = ServerTemplateProvider.class)
@Page
public class FriendFeedsPage extends Composite
        implements HasModel<Profile> {

    @Inject
    TransitionTo<WelcomePage> welcomePage;

    @Inject
    TransitionTo<LoginPage> loginPage;

    @Inject
    TransitionTo<MessagesPage> messagesPage;

    @Inject
    TransitionTo<SearchPage> searchPage;

    @Inject
    @DataField
    NavBar navBar;

    @Inject
    @DataField
    UserNavPanel userNav;

    @Inject
    @DataField
    FlowPanel statuses;

    @Inject
    Instance<StatusWidget> statusInstance;

    @Inject
    LoggedInUser loggedInUser;

    @Inject
    @DataField
    Image profilePic;

    @Inject
    @DataField
    Image coverPhoto;

    @Inject
    @DataField
    Button addFriendButton;

    @PageState
    String friend;

    private Profile profile;

    @PostConstruct
    public void buildUI(){
        StyleInjector.injectStylesheet(".friendscube-profile {width:100%; height:100%;}");
    }

    @PageShown
    public void ready(){
        setCurrentProfile();
    }

    @EventHandler("addFriendButton")
    public void addFriend(ClickEvent event){
        event.preventDefault();
        ActivityResourceProxy activityResourceProxy = GWT.create(ActivityResourceProxy.class);
        activityResourceProxy.getClientResource().setReference(ClientProxyHelper.restRootPath() + ActivityResourceProxy.ACTIVITY_URI + loggedInUser.getUser().getId() + "/activities");
        Activity activity = new Activity();
        activity.setAttributedTo(profile.getUser().getId());
        activity.setCreatorId(loggedInUser.getUser().getId());
        activity.setType(Activity.ActivityType.REQUEST.toString());
        activityResourceProxy.newActivity(activity, new Result<Void>() {
            @Override
            public void onFailure(Throwable throwable) {

            }
            @Override
            public void onSuccess(Void aVoid) {
                Window.alert("Request sent");
            }
        });
    }

    private void setStatusesToPage() {
        StatusesResourceProxy statusesResourceProxy = GWT.create(StatusesResourceProxy.class);
        statusesResourceProxy.getClientResource().setReference(ClientProxyHelper.restRootPath() + StatusesResourceProxy.STATUSES_URI);
        statusesResourceProxy.getClientResource().setChallengeResponse(ChallengeScheme.HTTP_BASIC, "Authorization", getClientToken());
        statusesResourceProxy.retrieve(new Result<StatusList>() {
            @Override
            public void onFailure(Throwable throwable) {
                Window.alert("Cannot retrieve statuses");
            }

            @Override
            public void onSuccess(StatusList statusList) {
                for (Status status : statusList.getStatuses()) {
                    StatusWidget w = statusInstance.get();
                    w.setProfile(profile);
                    w.setModel(status);
                    statuses.insert(w, 0);
                }
            }
        });
    }

    private void setCurrentProfile() {
        if (friend != null) {
            ProfileResourceProxy profileResourceProxy = GWT.create(ProfileResourceProxy.class);
            profileResourceProxy.getClientResource().setReference(ClientProxyHelper.restRootPath() + ProfileResourceProxy.PROFILE_URI + "friend");
            profileResourceProxy.getClientResource().addQueryParameter("client_token", getClientToken());
            profileResourceProxy.getClientResource().addQueryParameter("user_id", friend);
            profileResourceProxy.retrieve(new Result<Profile>() {
                @Override
                public void onFailure(Throwable throwable) {

                }
                @Override
                public void onSuccess(Profile result) {
                    profile = result;
                    //setProfileInfoToPage();
                    setStatusesToPage();

                }
            });
        } else {
            profile = loggedInUser.getUser().getProfile();
            //setProfileInfoToPage();
            setStatusesToPage();
            addFriendButton.setVisible(false);
        }
    }

    @Override
    public void setModel(Profile profile){
        this.profile = profile;
    }

    @Override
    public Profile getModel(){
        return this.profile;
    }

    private String getClientToken() {
        String token;
        String userToken = loggedInUser.getUser().getClientToken();
        if(userToken != null){
            token = userToken;
        } else {
            token = loggedInUser.getUser().getClientToken();
        }
        return token;
    }

}
