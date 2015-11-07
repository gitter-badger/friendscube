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
package com.dotweblabs.friendscube.app.client.local.widgets;

import com.dotweblabs.friendscube.app.client.local.LoggedInUser;
import com.dotweblabs.friendscube.app.client.local.util.ClientProxyHelper;
import com.dotweblabs.friendscube.app.client.local.util.CssHelper;
import com.dotweblabs.friendscube.app.client.shared.ProfileResourceProxy;
import com.dotweblabs.friendscube.app.client.shared.entity.Profile;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.inject.Inject;
import org.jboss.errai.ui.shared.ServerTemplateProvider;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.restlet.client.resource.Result;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;

/**
 * @author <a href="mailto:kerbymart@gmail.com">Kerby Martino</a>
 * @version 1.0
 * @since 1.0
 */
@Dependent
@Templated(value = "/WelcomePage.html#profileInfo", provider = ServerTemplateProvider.class)
public class ProfileInfoWidget extends Composite {

    @Inject
    @DataField
    Label profileFirstName;

    @Inject
    @DataField
    Label profileMiddleName;

    @Inject
    @DataField
    Label profileLastName;

    @Inject
    @DataField
    Label profileEmail;

    @Inject
    @DataField
    Label profilePhone;

    @Inject
    @DataField
    Label profileJobTitle;

    @Inject
    @DataField
    Label profileAboutMe;

    Profile profile;

    @Inject
    LoggedInUser loggedInUser;


    public void setProfile(Profile profile){
        this.profile = profile;
        profileFirstName.setText(profile.getFirstName());
        profileMiddleName.setText(profile.getMiddleName());
        profileLastName.setText(profile.getLastName());
        profileEmail.setText(profile.getEmail());
        profilePhone.setText(profile.getPhoneNumber());
        profileJobTitle.setText(profile.getJobTitle());
        profileAboutMe.setText(profile.getAboutMe());
    }

    @PostConstruct
    public void buildUI(){
        CssHelper.loadCss("js/jquery-editable/css/jquery-editable.css");
        StyleInjector.inject(".editable-click { border-bottom: none; }");
        ScriptInjector.fromUrl("js/jquery-editable/js/jquery-1.9.1.min.js").setWindow(ScriptInjector.TOP_WINDOW).inject();
        ScriptInjector.fromUrl("js/jquery-editable/js/jquery.poshytip.js").setWindow(ScriptInjector.TOP_WINDOW).inject();
        ScriptInjector.fromUrl("js/jquery-editable/js/jquery-editable-poshytip.js").setWindow(ScriptInjector.TOP_WINDOW).inject();
    }

    public void initEditableFields(){
        String clientToken = loggedInUser.getUser().getClientToken();
        initEditableFields(profileAboutMe.getElement(), profileEmail.getElement(), profilePhone.getElement(), profileFirstName.getElement(), profileMiddleName.getElement(), profileLastName.getElement(),profileJobTitle.getElement());
    }

    private void sendProfileInfo(String field, String value){
        ProfileResourceProxy profileResourceProxy = GWT.create(ProfileResourceProxy.class);
        profileResourceProxy.getClientResource().setReference(ClientProxyHelper.restRootPath() + ProfileResourceProxy.PROFILE_URI);
        profileResourceProxy.getClientResource().addQueryParameter("client_token", loggedInUser.getUser().getClientToken());

        final Profile profile = loggedInUser.getUser().getProfile();
        String firstName = field.equals("firstName") ? value : profileFirstName.getText();
        String middleName = field.equals("middleName") ? value : profileMiddleName.getText();
        String lastName = field.equals("lastName") ? value : profileLastName.getText();
        String jobTitle = field.equals("jobTitle") ? value : profileJobTitle.getText();
        String phoneNumber = field.equals("phoneNumber") ? value : profilePhone.getText();
        String email = field.equals("email") ? value : profileEmail.getText();
        String aboutMe = field.equals("aboutMe") ? value :  profileAboutMe.getText();

        profile.setFirstName(firstName);
        profile.setMiddleName(middleName);
        profile.setLastName(lastName);
        profile.setJobTitle(jobTitle);
        profile.setPhoneNumber(phoneNumber);
        profile.setEmail(email);
        profile.setAboutMe(aboutMe);

        profileResourceProxy.store(profile, new Result<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                Window.alert("Cannot update profile");
            }

            @Override
            public void onSuccess(Void result) {
                // do nothing
            }
        });
    }

    private native void initEditableFields(Element profileAboutMe, Element profileEmail, Element profilePhone, Element profileFirstName, Element profileMiddleName, Element profileLastName, Element profileJobTitle)/*-{
        var objInstance = this;
        $wnd.sendProfile = function(e, params){
            objInstance.@ProfileInfoWidget::sendProfileInfo(*)(params);
        };
        $wnd.$.fn.editable.defaults.mode = 'inline';
        $wnd.$('#'+profileAboutMe.id).editable();
        $wnd.$('#'+profileAboutMe.id).on('save', function(e, params){
            objInstance.@ProfileInfoWidget::sendProfileInfo(*)('aboutMe', params.newValue);
        });
        $wnd.$('#'+profileEmail.id).editable();
        $wnd.$('#'+profileEmail.id).on('save', function(e, params){
            objInstance.@ProfileInfoWidget::sendProfileInfo(*)('email', params.newValue);
        });
        $wnd.$('#'+profileFirstName.id).editable();
        $wnd.$('#'+profileFirstName.id).on('save', function(e, params){
            objInstance.@ProfileInfoWidget::sendProfileInfo(*)('firstName', params.newValue);
        });
        $wnd.$('#'+profileMiddleName.id).editable();
        $wnd.$('#'+profileMiddleName.id).on('save', function(e, params){
            objInstance.@ProfileInfoWidget::sendProfileInfo(*)('middleName', params.newValue);
        });
        $wnd.$('#'+profileLastName.id).editable();
        $wnd.$('#'+profileLastName.id).on('save', function(e, params){
            objInstance.@ProfileInfoWidget::sendProfileInfo(*)('lastName', params.newValue);
        });
        $wnd.$('#'+profileJobTitle.id).editable();
        $wnd.$('#'+profileJobTitle.id).on('save', function(e, params){
            objInstance.@ProfileInfoWidget::sendProfileInfo(*)('jobTitle', params.newValue);
        });
        $wnd.$('#'+profilePhone.id).editable();
        $wnd.$('#'+profilePhone.id).on('save', function(e, params){
            objInstance.@ProfileInfoWidget::sendProfileInfo(*)('phoneNumber', params.newValue);
        });
    }-*/;

}
