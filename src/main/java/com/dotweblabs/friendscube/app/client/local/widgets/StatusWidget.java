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
import com.dotweblabs.friendscube.app.client.local.widgets.like.LikeButton;
import com.dotweblabs.friendscube.app.client.shared.entity.Profile;
import com.dotweblabs.friendscube.app.client.shared.entity.actions.Like;
import com.dotweblabs.friendscube.app.client.shared.entity.actions.Status;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import org.jboss.errai.ui.client.widget.HasModel;
import org.jboss.errai.ui.shared.ServerTemplateProvider;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
/**
 * @author <a href="mailto:kerbymart@gmail.com">Kerby Martino</a>
 * @version 1.0
 * @since 1.0
 */
@Dependent
@Templated(value = "/WelcomePage.html#statusItem", provider = ServerTemplateProvider.class)
public class StatusWidget extends Composite implements HasModel<Status> {

    @Inject
    @DataField
    Label message;

    @Inject
    @DataField
    Label time;

    @Inject
    @DataField
    Label profileName;

    @Inject
    @DataField
    LikeButton like;

    @Inject
    LoggedInUser loggedInUser;

    Status status;

    Profile profile;

    @Override
    public Status getModel() {
        return status;
    }

    @Override
    public void setModel(Status status) {
        this.status = status;
        message.setText(status.getMessage());
        profileName.setText(profile.getFirstName()+" "+profile.getMiddleName()+" " +profile.getLastName());
        time.setText(DateTimeFormat.getFormat("dd/MM/yyyy h:mm a").format(status.getCreated()));
        like.setItemType(Like.LikeType.STATUS.toString());
        like.setItemId(status.getId());
        like.setUserId(loggedInUser.getUser().getId());

    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
