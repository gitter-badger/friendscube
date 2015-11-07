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

import com.dotweblabs.friendscube.app.client.shared.entity.relationships.Follower;
import com.dotweblabs.friendscube.app.client.shared.entity.relationships.Followers;
import com.dotweblabs.friendscube.rest.services.FollowService;
import com.hunchee.twist.types.ListResult;

import static com.hunchee.twist.ObjectStoreService.store;

public class GaeFollowService implements FollowService {
    @Override
    public Follower createFollower(Follower follower) {
        store().put(follower);
        return follower;
    }

    @Override
    public Follower readFollower(Long id) {
        return null;
    }

    @Override
    public Follower updateFollower(Follower follower) {
        return null;
    }

    @Override
    public Followers listFollowers(Long userId) {
        ListResult<Follower> result = store().find(Follower.class)
                .equal("followee", userId)
                .limit(20)
                .asList();
        Followers followers = new Followers();
        followers.setCount((long) result.getList().size());
        followers.setFollowers(result.getList());
        return followers;
    }

    @Override
    public void deleteFollower(Long id) {

    }

    @Override
    public boolean follow(Long follower, Long followee) {
        return false;
    }

    @Override
    public boolean unfollow(Long follower, Long followee) {
        return false;
    }

    @Override
    public boolean blockFollower(Long follower) {
        return false;
    }
}
