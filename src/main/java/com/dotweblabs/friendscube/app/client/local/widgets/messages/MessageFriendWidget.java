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
package com.dotweblabs.friendscube.app.client.local.widgets.messages;

import com.dotweblabs.friendscube.app.client.shared.entity.Profile;
import com.dotweblabs.friendscube.app.client.shared.entity.User;
import com.dotweblabs.friendscube.app.client.shared.entity.actions.Message;
import com.dotweblabs.friendscube.app.client.shared.entity.relationships.Friend;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import org.jboss.errai.ui.client.widget.HasModel;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.Date;

/**
 * @author <a href="mailto:kerbymart@gmail.com">Kerby Martino</a>
 * @version 1.0
 * @since 1.0
 */
@Dependent
@Templated
public class MessageFriendWidget extends Composite implements HasModel<Friend> {
    @Inject
    @DataField
    Label msg;

    @Inject
    @DataField
    Label timeSent;

    @Inject
    @DataField
    Label profileName;

    Profile profile;
    Friend model;
    Message message;

    public void setMessage(User message){
        Window.alert("1");
      //  this.message= message;
        msg.setText("Awu!!.. Awu!!..");
        profileName.setText(profile.getFirstName()+" "+profile.getMiddleName()+" " +profile.getLastName());
        timeSent.setText(DateTimeFormat.getFormat("h:mm a").format(new Date()));
        Window.alert("2");
    }
    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    @EventHandler
    public void alert(ClickEvent event){
        event.preventDefault();
        Window.alert(profileName.getText());
    }


    @Override
    public Friend getModel() {
        return model;
    }

    @Override
    public void setModel(Friend friend) {
        this.model=friend;
    }

}


