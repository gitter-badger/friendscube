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
package com.dotweblabs.friendscube.rest.utils;

import com.dotweblabs.friendscube.app.client.shared.entity.Profile;
import com.dotweblabs.friendscube.app.client.shared.entity.User;
import com.dotweblabs.friendscube.app.client.shared.entity.relationships.Friend;
import org.fluttercode.datafactory.impl.DataFactory;

import java.util.Date;

/**
 * @author <a href="mailto:kerbymart@gmail.com">Kerby Martino</a>
 * @version 1.0
 * @since 1.0
 */
public class TestData {
    public static User createDemoUser(String username){
        DataFactory df = new DataFactory();
        User demo = new User();
        // plain text: 12345678
        demo.setPasswordHash("$2a$10$rq/OQVs9sbG6.tiTdIyTZOdR8oPcIMHUxLRrUfEbddgXXMt6x3vUm");
        demo.setUsername(username);
        demo.setCreated(new Date());
        demo.setModified(new Date());
        demo.setDomain("friendscube"); // should be 'friendscube.com'

        Profile demoProfile = new Profile();
        demoProfile.setCreated(new Date());
        demoProfile.setModified(new Date());
        demoProfile.setAboutMe("This is a demo account");
        demoProfile.setEmail("demo@friendscube.com");
        demoProfile.setFirstName(df.getFirstName());
        demoProfile.setMiddleName("");
        demoProfile.setLastName(df.getLastName());
        demoProfile.setJobTitle(df.getBusinessName());

        demo.setProfile(demoProfile);

        return demo;
    }
    public static Friend createDemoFriendship(Long userId, Long friendId){
        Friend friend = new Friend();
        friend.setCreated(new Date());
        friend.setModified(new Date());
        friend.setAccepted(true);
        friend.setUserId(userId);
        friend.setFriendId(friendId);
        return friend;
    }
}
