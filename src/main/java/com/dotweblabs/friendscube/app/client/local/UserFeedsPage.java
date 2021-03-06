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
import com.dotweblabs.friendscube.app.client.shared.ActivityResourceProxy;
import com.dotweblabs.friendscube.app.client.shared.ProfileResourceProxy;
import com.dotweblabs.friendscube.app.client.shared.StatusesResourceProxy;
import com.dotweblabs.friendscube.app.client.shared.entity.Profile;
import com.dotweblabs.friendscube.app.client.shared.entity.actions.StatusList;
import com.dotweblabs.friendscube.app.client.shared.entity.activities.Activity;
import com.dotweblabs.friendscube.app.client.local.widgets.ProfileInfoWidget;
import com.dotweblabs.friendscube.app.client.local.widgets.StatusWidget;
import com.dotweblabs.friendscube.app.client.local.widgets.UserNavPanel;
import com.dotweblabs.friendscube.app.client.shared.entity.actions.Status;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
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
@Templated(value = "/UserFeedsPage.html#userfeeds", provider = ServerTemplateProvider.class)
@Page
public class UserFeedsPage extends Composite {

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

//    @Inject
//    @DataField
//    ProfileInfoWidget profileInfo;

    @Inject
    @DataField
    Image profilePic;

    @Inject
    @DataField
    Image coverPhoto;

    private Profile profile;


    @PostConstruct
    public void buildUI(){
        StyleInjector.injectStylesheet(".friendscube-profile {width:100%; height:100%;}");
    }

    @PageShown
    public void ready(){
        setCurrentProfile();
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

//    private void setProfileInfoToPage() {
//        profileInfo.setProfile(profile);
//        if(profile.getPhoto() != null && !profile.getPhoto().isEmpty()){
//            profilePic.getElement().setAttribute("src", profile.getPhoto());
//        }
//        if(profile.getCoverPhoto() != null && !profile.getCoverPhoto().isEmpty()){
//            coverPhoto.getElement().setAttribute("src", profile.getCoverPhoto());
//        }
//    }

    private String getClientToken() {
        return loggedInUser.getUser().getClientToken();
    }

    private void setCurrentProfile() {
        String userToken = loggedInUser.getUser().getClientToken();
        if(userToken != null){
            ProfileResourceProxy profileResourceProxy = GWT.create(ProfileResourceProxy.class);
            profileResourceProxy.getClientResource().setReference(ClientProxyHelper.restRootPath() + ProfileResourceProxy.PROFILE_URI);
            profileResourceProxy.getClientResource().addQueryParameter("client_token", userToken);
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
        }
    }

}
