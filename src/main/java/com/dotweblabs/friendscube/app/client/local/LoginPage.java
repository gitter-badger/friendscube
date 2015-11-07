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

import com.dotweblabs.friendscube.app.client.local.util.Base64;
import com.dotweblabs.friendscube.app.client.local.util.ClientProxyHelper;
import com.dotweblabs.friendscube.app.client.local.util.CssHelper;
import com.dotweblabs.friendscube.app.client.local.widgets.ResetPasswordModal;
import com.dotweblabs.friendscube.app.client.shared.TokensResourceProxy;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.dom.client.StyleElement;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.dotweblabs.friendscube.app.client.local.widgets.LoggedOutNavBar;
import com.dotweblabs.friendscube.app.client.shared.RegistrationResourceProxy;
import com.dotweblabs.friendscube.app.client.shared.UserResourceProxy;
import com.dotweblabs.friendscube.app.client.shared.entity.Registration;
import com.dotweblabs.friendscube.app.client.shared.entity.User;
import org.jboss.errai.ui.nav.client.local.*;
import org.jboss.errai.ui.shared.ServerTemplateProvider;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.restlet.client.data.MediaType;
import org.restlet.client.data.Method;
import org.restlet.client.resource.Result;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.Date;

import static com.google.gwt.query.client.GQuery.*;

/**
 * @author <a href="mailto:kerbymart@gmail.com">Kerby Martino</a>
 * @version 1.0
 * @since 1.0
 */
@Dependent
@Templated(value = "/templates/LoginPage.html#login", provider = ServerTemplateProvider.class)
@Page
public class LoginPage extends Composite {

    @Inject
    TransitionTo<WelcomePage> welcomePage;

    @Inject
    TransitionTo<MessagesPage> messagesPage;

    @Inject
    TransitionTo<SearchPage> searchPage;

    @Inject
    TransitionTo<UserFeedsPage> userFeeds;

    @Inject
    @DataField
    LoggedOutNavBar navBar;

    @Inject
    @DataField
    Button loginButton;

    @Inject
    @DataField
    Button facebookLogin;

    @Inject
    @DataField
    TextBox username;

    @Inject
    @DataField
    PasswordTextBox password;

    @Inject
    @DataField
    TextBox registrationEmail;

    @Inject
    @DataField
    PasswordTextBox registrationPassword;

    @Inject
    @DataField
    TextBox firstName;

    @Inject
    @DataField
    TextBox lastName;

    @Inject
    @DataField
    TextBox middleName;

    @Inject
    @DataField
    TextBox birthDate;

    @Inject
    @DataField
    Button registerButton;

    @Inject
    @DataField
    ResetPasswordModal resetPasswordModal;

    @Inject
    @DataField
    Anchor resetPasswordLink;

    @Inject
    LoggedInUser loggedInUser;

    @PageState
    String email;

    @PageState
    String action;

    private StyleElement style;

    @PageShowing
    public void showing(){
        style = StyleInjector.injectStylesheet(
                        "html { background: url(img/form_bg.jpg) no-repeat center center fixed; -webkit-background-size: cover; -moz-background-size: cover;-o-background-size: cover; background-size: cover; } " +
                        "body { background:transparent; }");
        loadCss();
        loadJS();
    }


    private void loadJS() {
        ScriptInjector.fromUrl("js/plugins.js").inject();
        ScriptInjector.fromUrl("js/main.js").inject();
        ScriptInjector.fromUrl("uikit-assets/js/addons/datepicker.js").inject();
        ScriptInjector.fromUrl("uikit-assets/js/addons/datepicker.min.js").inject();
        ScriptInjector.fromUrl("uikit-assets/js/addons/form-password.js").inject();
        ScriptInjector.fromUrl("js/vendor/jquery-1.10.2.min.js").inject();
        ScriptInjector.fromUrl("js/jquery-ui.js").inject();
        ScriptInjector.fromUrl("js/jquery.js").inject();
        ScriptInjector.fromUrl("js/plugins.js").inject();
        ScriptInjector.fromUrl("uikit-assets/js/uikit.min.js").inject();
        ScriptInjector.fromUrl("uikit-assets/js/addons/sticky.js").inject();
    }


    @PageHiding
    public void hiding(){
        style.removeFromParent();
    }

    @PageShown
    public void ready(){
        username.setText(email);
        initAdditionalJS();
        if("logout".equals(action)){
            $("#loggedout-alert").removeClass("uk-hidden");
        }
    }


    @PostConstruct
    public void buildUI(){
    }

    @EventHandler("registerButton")
    public void register(ClickEvent event){
        event.preventDefault();
        String email = registrationEmail.getText();
        String password = registrationPassword.getText();
        RegistrationResourceProxy registrationResource = GWT.create(RegistrationResourceProxy.class);

        registrationResource.getClientResource().setReference(ClientProxyHelper.restRootPath() + RegistrationResourceProxy.REGISTRATIONS_URI);
        registrationResource.getClientResource().setMethod(Method.POST);
        Registration registration = new Registration(email, Base64.byteArrayToBase64(password.getBytes()));
        registration.setFirstName(firstName.getText());

        registration.setLastName(lastName.getText());
        registration.setMiddleName(middleName.getText());
        registration.setBirthDate(DateTimeFormat.getFormat("yyyy-MM-dd").parse(birthDate.getText()));
        if ((firstName.getText().isEmpty()) || (registration.getLastName().isEmpty()) || (registration.getPassword().isEmpty()) || (registration.getEmail().isEmpty())){
            $("#register-error").removeClass("uk-hidden")
                    .text("Please complete all the information below.");
            return;
        }
        if ((password.length()<8) || (password.length()>15)){
            $("#register-error").removeClass("uk-hidden")
                    .html("<a class=\"uk-alert-close uk-close\"></a> Password must be composed of 8 to 15 characters.");
            return;
        }
        registrationResource.getClientResource().accept(MediaType.APPLICATION_JSON);
        registrationResource.getClientResource().accept(MediaType.APPLICATION_JAVA_OBJECT_GWT);
        registrationResource.store(registration, new Result<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                //Logger.consoleLog("Error: " + caught.getMessage() + ", " + caught.getStackTrace());
                $("#register-error").removeClass("uk-hidden")
                        .text("Registration is failed.");
            }

            @Override
            public void onSuccess(Void result) {
                Window.alert("Registered!. Please log in now ");
            }
        });



    }

    @EventHandler("resetPasswordLink")
    public void resetPassword(ClickEvent event){
        event.preventDefault();
        resetPasswordModal.show();
    }

    @EventHandler("loginButton")
    public void login(ClickEvent event) {
        event.preventDefault();
        String uname = username.getText();
        String pass = password.getText();
        if ((uname.isEmpty() || pass.isEmpty())){
            $("#login-alert").removeClass("uk-hidden")
                    .text("Please enter username/password.");
            return;
        }

        // DEMO
        if(uname.equals("demo") && pass.equals("demo")){
            User user = new User();
            user.setId(0L);
            user.setUsername("demo");
            loggedInUser.setUser(user);
            Multimap<String, String> state = ArrayListMultimap.create();
            state.put("token", String.valueOf(user.getClientToken()));
            long oneDay = 24 * 60 * 60 * 1000;
            Date expiration = new Date(new Date().getTime() + oneDay);
            Cookies.setCookie("token", String.valueOf(user.getClientToken()), expiration);
            welcomePage.go(state);
        }

        TokensResourceProxy tokensResource = GWT.create(TokensResourceProxy.class);
        tokensResource.getClientResource().setReference(ClientProxyHelper.restRootPath() + TokensResourceProxy.TOKENS_URI);
        // TODO: This is not really a good practice!
        tokensResource.getClientResource().addQueryParameter("domain", UserResourceProxy.DOMAIN);
        tokensResource.getClientResource().addQueryParameter("email", uname);
        tokensResource.getClientResource().addQueryParameter("password", Base64.byteArrayToBase64(pass.getBytes()));


        tokensResource.retrieve(new Result<User>() {
            @Override
            public void onFailure(Throwable throwable) {
                $("#login-alert").removeClass("uk-hidden")
                        .text("Server error when logging in. Please try again.");
            }

            @Override
            public void onSuccess(User user) {
                if (user != null) {
                    loggedInUser.setUser(user);
                    Multimap<String, String> state = ArrayListMultimap.create();
                    state.put("token", String.valueOf(user.getClientToken()));
                    long oneDay = 24 * 60 * 60 * 1000;
                    Date expiration = new Date(new Date().getTime() + oneDay);
                    Cookies.setCookie("token", String.valueOf(user.getClientToken()), expiration);
                    welcomePage.go(state);
                } else {
                    $("#login-alert").removeClass("uk-hidden")
                            .text("Incorrect username/password. Please try again.");
                }
            }
        });
    }

    @EventHandler("facebookLogin")
    public void facebookLogin(ClickEvent event) {
        // TODO
    }


    private void loadCss() {
        CssHelper.loadCss("uikit-assets/css/uikit.min.css");
        CssHelper.loadCss("uikit-assets/css/uikit.almost-flat.css");
        CssHelper.loadCss("uikit-assets/css/components/datepicker.css");
        CssHelper.loadCss("uikit-assets/css/components/form-password.css");
        CssHelper.loadCss("uikit-assets/css/components/form-password.almost-flat.css");
        CssHelper.loadCss("uikit-assets/css/components/form-password.gradient.css");
        CssHelper.loadCss("uikit-assets/css/components/form-password.gradient.css");
        CssHelper.loadCss("uikit-assets/css/components/datepicker.min.css");
        CssHelper.loadCss("uikit-assets/css/components/datepicker.almost-flat.css");
        CssHelper.loadCss("uikit-assets/css/components/datepicker.almost-flat.min.css");
        CssHelper.loadCss("uikit-assets/css/components/datepicker.gradient.css");
        CssHelper.loadCss("uikit-assets/css/components/datepicker.gradient.min.css");
        CssHelper.loadCss("uikit-assets/css/uikit.min.css");
        CssHelper.loadCss("uikit-assets/css/uikit.almost-flat.css");
        CssHelper.loadCss("uikit-assets/css/components/datepicker.css");
        CssHelper.loadCss("uikit-assets/css/components/form-password.css");
        CssHelper.loadCss("uikit-assets/css/components/form-password.almost-flat.css");
        CssHelper.loadCss("uikit-assets/css/components/form-password.gradient.css");
        CssHelper.loadCss("uikit-assets/css/components/form-password.gradient.css");
        CssHelper.loadCss("uikit-assets/css/components/datepicker.min.css");
        CssHelper.loadCss("uikit-assets/css/components/datepicker.almost-flat.css");
        CssHelper.loadCss("uikit-assets/css/components/datepicker.almost-flat.min.css");
        CssHelper.loadCss("uikit-assets/css/components/datepicker.gradient.css");
        CssHelper.loadCss("uikit-assets/css/components/datepicker.gradient.min.css");
        CssHelper.loadCss("css/font-awesome/css/font-awesome.css");
        CssHelper.loadCss("css/octicons/octicons.css");
        CssHelper.loadCss("css/template.css");
    }

    private void initAdditionalJS() {
        $("#create-btn").on("click", new Function() {
            public boolean f(Event e) {
                $(".create-icon").toggleClass("uk-icon-spinner uk-icon-spin");
                return true;
            }
        });
        $("#login-btn").on("click", new Function() {
            public boolean f(Event e) {
                $(".login-icon").toggleClass("uk-icon-spinner uk-icon-spin");
                return true;
            }
        });
        $("#cancel").on("click", new Function() {
            public void f() {
                $(".email-modal").val("");
                $("#confirm-pass").val("");
                $("#pass").val("");
            }
        });
        $(".toggle-pass").on("click", new Function() {
            public boolean f(Event e) {
                e.preventDefault();
                if ($("#pass-box").hasClass("showpass")) {
                    GQuery oldElem = $("#pass-box");
                    GQuery newElem = oldElem.clone();
                    newElem.attr("type", "text");
                    oldElem.replaceWith(newElem);
                    $("#pass-box").removeClass("showpass");
                    $("#create-pw").text("Hide");
                } else {
                    GQuery oldElem = $("#pass-box");
                    GQuery newElem = oldElem.clone();
                    newElem.attr("type", "password");
                    oldElem.replaceWith(newElem);
                    $("#pass-box").addClass("showpass");
                    $("#create-pw").text("Show");
                }
                return true;
            }
        });
        $(".renew-pw").on("click", new Function() {
            public boolean f(Event e) {
                e.preventDefault();
                if ($("#pass").hasClass("showpass")) {
                    GQuery oldElem = $("#pass");
                    GQuery newElem = oldElem.clone();
                    newElem.attr("type", "text");
                    oldElem.replaceWith(newElem);
                    $("#pass").removeClass("showpass");
                    $("#renew-pw").text("Hide");
                } else {
                    GQuery oldElem = $("#pass");
                    GQuery newElem = oldElem.clone();
                    newElem.attr("type", "password");
                    oldElem.replaceWith(newElem);
                    $("#pass").addClass("showpass");
                    $("#renew-pw").text("Show");
                }
                return true;
            }
        });
        $(".logpass").on("click", new Function() {
            public boolean f(Event e) {
                e.preventDefault();
                if ($("#login-pass").hasClass("showpass")) {
                    GQuery oldElem = $("#login-pass");
                    GQuery newElem = oldElem.clone();
                    newElem.attr("type", "text");
                    oldElem.replaceWith(newElem);
                    $("#login-pass").removeClass("showpass");
                    $("#log-pw").text("Hide");
                } else {
                    GQuery oldElem = $("#login-pass");
                    GQuery newElem = oldElem.clone();
                    newElem.attr("type", "password");
                    oldElem.replaceWith(newElem);
                    $("#login-pass").addClass("showpass");
                    $("#log-pw").text("Show");
                }
                return true;
            }
        });
        $(".confirm-pw").on("click", new Function() {
            public boolean f(Event e) {
                e.preventDefault();
                if ($("#confirm-pass").hasClass("showpass")) {
                    GQuery oldElem = $("#confirm-pass");
                    GQuery newElem = oldElem.clone();
                    newElem.attr("type", "text");
                    oldElem.replaceWith(newElem);
                    $("#confirm-pass").removeClass("showpass");
                    $("#confirm-pw").text("Hide");
                } else {
                    GQuery oldElem = $("#confirm-pass");
                    GQuery newElem = oldElem.clone();
                    newElem.attr("type", "password");
                    oldElem.replaceWith(newElem);
                    $("#confirm-pass").addClass("showpass");
                    $("#confirm-pw").text("Show");
                }
                return true;
            }
        });
        //Logger.consoleLog("loaded JS");
    }


}
