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
package com.dotweblabs.friendscube.rest.services;

import com.dotweblabs.friendscube.app.client.shared.entity.relationships.Follower;
import com.dotweblabs.friendscube.app.client.shared.entity.relationships.Followers;
/**
 * @author <a href="mailto:kerbymart@gmail.com">Kerby Martino</a>
 * @version 1.0
 * @since 1.0
 */
public interface FollowService {
    public Follower createFollower(Follower follower);
    public Follower readFollower(Long id);
    public Follower updateFollower(Follower follower);
    public Followers listFollowers(Long userId);
    public void deleteFollower(Long id);
    public boolean follow(Long follower, Long followee);
    public boolean unfollow(Long follower, Long followee);
    public boolean blockFollower(Long follower);
}
