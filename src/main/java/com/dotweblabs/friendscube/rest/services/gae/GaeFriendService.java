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
package com.dotweblabs.friendscube.rest.services.gae;

import com.dotweblabs.friendscube.app.client.shared.entity.relationships.Friend;
import com.dotweblabs.friendscube.app.client.shared.entity.relationships.Friends;
import com.dotweblabs.friendscube.rest.services.FriendService;
import com.hunchee.twist.types.ListResult;

import static com.hunchee.twist.ObjectStoreService.store;

public class GaeFriendService implements FriendService {
    @Override
    public void addFriend(Friend friend) {
        store().put(friend);
    }

    @Override
    public void updateFriend(Friend friend) {

    }

    @Override
    public Friend readFriend(Long friendId) {
        return null;
    }

    @Override
    public Friend readFriend(Long friendId, Long userId) {
        Friend friend = (Friend) store().find(Friend.class)
                .equal("userId", userId)
                .equal("friendId", friendId)
                .first();
        return friend;
    }

    @Override
    public Friends listFriends(Long userId) {
        ListResult<Friend> result = store().find(Friend.class)
                .equal("userId", userId)
                .limit(20)
                .asList();
        Friends friends = new Friends();
        friends.setFriends(result.getList());
        friends.setCount(Integer.valueOf(result.getList().size()).longValue());
        return friends;
    }


    @Override
    public void deleteFriend(Long friendId) {

    }

    @Override
    public void blockFriend(Long friendId) {

    }
}
