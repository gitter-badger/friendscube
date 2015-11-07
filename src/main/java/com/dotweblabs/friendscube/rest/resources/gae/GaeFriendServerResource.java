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
package com.dotweblabs.friendscube.rest.resources.gae;

import com.dotweblabs.friendscube.app.client.shared.entity.relationships.Friend;
import com.dotweblabs.friendscube.rest.guice.SelfInjectingServerResource;
import com.dotweblabs.friendscube.rest.resources.FriendResource;
import com.dotweblabs.friendscube.rest.services.FriendService;
import com.google.inject.Inject;
/**
 * @author <a href="mailto:kerbymart@gmail.com">Kerby Martino</a>
 * @version 1.0
 * @since 1.0
 */
public class GaeFriendServerResource extends SelfInjectingServerResource
    implements FriendResource {

    @Inject
    FriendService friendService;

    @Override
    public Friend getFriend() {
        Long userId = Long.valueOf(this.getRequestAttributes().get("user_id").toString());
        Long friendId = Long.valueOf(this.getRequestAttributes().get("friend_id").toString());
        Friend friend = friendService.readFriend(friendId, userId);
        return friend;
    }
}
