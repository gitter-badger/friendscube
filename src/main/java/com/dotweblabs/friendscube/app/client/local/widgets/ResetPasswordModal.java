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

import com.dotweblabs.friendscube.app.client.local.util.Base64;
import com.dotweblabs.friendscube.app.client.local.util.ClientProxyHelper;
import com.dotweblabs.friendscube.app.client.shared.RegistrationResourceProxy;
import com.dotweblabs.friendscube.app.client.shared.entity.Registration;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.inject.Inject;
import org.jboss.errai.ui.shared.ServerTemplateProvider;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.restlet.client.resource.Result;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import static com.google.gwt.query.client.GQuery.*;

/**
 * @author <a href="mailto:kerbymart@gmail.com">Kerby Martino</a>
 * @version 1.0
 * @since 1.0
 */
@Dependent
@Templated(value = "/LoginPage.html#resetPasswordModal", provider = ServerTemplateProvider.class)
public class ResetPasswordModal extends Composite {

    @Inject
    @DataField
    TextBox email;

    @Inject
    @DataField
    PasswordTextBox newPassword;

    @Inject
    @DataField
    PasswordTextBox confirmPassword;

    @Inject
    @DataField
    Button resetPassword;

    @PostConstruct
    public void buildUI(){

    }

    public void show(){
        showModal(this.getElement());
    }

    private static native void showModal(Element el)/*-{
        var modal = $wnd.UIkit.modal(el);
        if ( modal.isActive() ) {
            modal.hide();
        } else {
            modal.show();
        }
    }-*/;

    @EventHandler("resetPassword")
    public void resetPassword(ClickEvent event){
        event.preventDefault();
        if(isPasswordFieldsEqual()){
            RegistrationResourceProxy registrationResourceProxy = GWT.create(RegistrationResourceProxy.class);
            registrationResourceProxy.getClientResource().setReference(ClientProxyHelper.restRootPath() + RegistrationResourceProxy.REGISTRATIONS_URI);
            registrationResourceProxy.getClientResource().addQueryParameter("is_forgot","true");

            Registration registration = new Registration(email.getText(), Base64.byteArrayToBase64(newPassword.getText().getBytes()));

            registrationResourceProxy.store(registration, new Result<Void>() {
                @Override
                public void onFailure(Throwable throwable) {
                    $("#reset-failed-msg p").text("Failed to reset password");
                    $("#reset-failed-msg").removeClass("uk-hidden");
                }

                @Override
                public void onSuccess(Void result) {
                    $("#reset-success-msg p").text("Success! Please check email for confirmation.");
                    $("#reset-success-msg").removeClass("uk-hidden");
                }
            });

        } else {
            $("#reset-failed-msg p").text("Password fields are blank or not the same");
            $("#reset-failed-msg").removeClass("uk-hidden");
        }
    }

    private boolean isPasswordFieldsEqual() {
        return !newPassword.getText().isEmpty() && !confirmPassword.getText().isEmpty() && newPassword.getText().equals(confirmPassword.getText());
    }
}
