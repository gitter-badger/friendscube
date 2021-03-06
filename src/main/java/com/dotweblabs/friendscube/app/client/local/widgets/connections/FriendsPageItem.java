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
package com.dotweblabs.friendscube.app.client.local.widgets.connections;

import com.dotweblabs.friendscube.app.client.local.FriendFeedsPage;
import com.dotweblabs.friendscube.app.client.shared.entity.Profile;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
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
public class FriendsPageItem extends Composite{

    @Inject
    TransitionTo<FriendFeedsPage> friendFeedsPage;

    @Inject
    @DataField
    Anchor userFullName;

    @Inject
    @DataField
    Label userJobTitle;

    @Inject
    @DataField
    Image profilePic;

    Profile profile;

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
        String src = profile.getPhoto();
        if(src == null || src.equalsIgnoreCase("null")){
            src = "img/friend.jpg";
        }
        profilePic.getElement().setAttribute("src", src);
        userFullName.setText(profile.getFirstName() + " " +profile.getMiddleName() + " " +profile.getLastName());
        userJobTitle.setText(profile.getJobTitle());
    }

    @EventHandler("userFullName")
    public void viewFriend(ClickEvent event){
        event.preventDefault();
        Multimap<String, String> state = ArrayListMultimap.create();
        state.put("friend", String.valueOf(profile.getUserId()));
        friendFeedsPage.go(state);
    }
}
