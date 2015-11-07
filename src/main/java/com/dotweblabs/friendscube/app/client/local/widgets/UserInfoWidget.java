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

import com.dotweblabs.friendscube.app.client.local.UserFeedsPage;
import com.dotweblabs.friendscube.app.client.shared.entity.User;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import org.jboss.errai.ui.client.widget.HasModel;
import org.jboss.errai.ui.nav.client.local.TransitionTo;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

/**
 * @author <a href="mailto:kerbymart@gmail.com">Kerby Martino</a>
 * @version 1.0
 * @since 1.0
 */
@Dependent
@Templated
public class UserInfoWidget extends Composite
        implements HasModel<User> {

    @Inject
    TransitionTo<UserFeedsPage> userFeedsPage;

    @DataField
    @Inject
    Anchor userFeedsLink;

    @Inject
    @DataField
    Label userJobTitle;

    User model;

    @Override
    public User getModel() {
        return model;
    }

    @Override
    public void setModel(User user) {
        model = user;
        userFeedsLink.setText(model.getProfile().getFirstName() + " " + model.getProfile().getLastName());
        userJobTitle.setText(model.getProfile().getJobTitle());

    }

    @EventHandler("userFeedsLink")
    public void feeds(ClickEvent event){
        event.preventDefault();
        userFeedsPage.go();
    }
}
