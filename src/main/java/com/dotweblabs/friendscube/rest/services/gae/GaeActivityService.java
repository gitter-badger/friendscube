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

import com.dotweblabs.friendscube.app.client.shared.entity.Notification;
import com.dotweblabs.friendscube.app.client.shared.entity.User;
import com.dotweblabs.friendscube.app.client.shared.entity.activities.Activity;
import com.dotweblabs.friendscube.app.client.shared.entity.relationships.Follower;
import com.dotweblabs.friendscube.app.client.shared.entity.relationships.Friend;
import com.dotweblabs.friendscube.rest.services.ActivityService;
import com.dotweblabs.friendscube.rest.services.FollowService;
import com.dotweblabs.friendscube.rest.services.FriendService;
import com.dotweblabs.friendscube.rest.services.UserService;
import com.google.appengine.api.datastore.Key;
import com.google.inject.Inject;
import com.hunchee.twist.types.Function;

import java.util.Date;
import java.util.List;

import static com.hunchee.twist.ObjectStoreService.store;
/**
 * @author <a href="mailto:kerbymart@gmail.com">Kerby Martino</a>
 * @version 1.0
 * @since 1.0
 */
public class GaeActivityService implements ActivityService {

    @Inject
    UserService userService;

    @Inject
    FriendService friendService;

    @Inject
    FollowService followService;

    private void setVerb(Activity activity){
        if(Activity.ActivityType.REQUEST.toString().equals(activity.getType())){
            User user  = userService.read(activity.getCreatorId());
            activity.setVerb(user.getProfile().getFirstName() +" sent you a friend request.");
        } else if(Activity.ActivityType.ACCEPT.toString().equals(activity.getType())){
            User user  = userService.read(activity.getCreatorId());
            activity.setVerb(user.getProfile().getFirstName() +" accepted your friend request.");
        }
    }

    @Override
    public Activity createActivity(final Activity activity) {

        store().transact(new Function<Key>() {
            @Override
            public Key execute() {
                setVerb(activity);
                Key activityKey = store().put(activity);

                Notification notification = new Notification();
                notification.setCreated(new Date());
                notification.setUserId(activity.getAttributedTo());
                notification.setSeen(false);
                notification.setReferenceActivityId(activity.getActivityId());
                store().put(notification);

                return activityKey;
            }
        });
        processActivity(activity);
        return activity;
    }

    private void processActivity(Activity activity) {
        System.out.println("activity type: " + activity.getType());
        if(Activity.ActivityType.ACCEPT.toString().equals(activity.getType())){
            System.out.println("new Friend");
            Friend friend1 = new Friend();
            friend1.setUserId(activity.getCreatorId());
            friend1.setFriendId(activity.getAttributedTo());
            friend1.setAccepted(true);
            friend1.setBlocked(false);
            friendService.addFriend(friend1);

            Follower follower1 = new Follower();
            follower1.setCreated(new Date());
            follower1.setFollower(activity.getCreatorId());
            follower1.setFollowee(activity.getAttributedTo());
            follower1.setBlocked(false);
            followService.createFollower(follower1);

            Friend friend2 = new Friend();
            friend2.setUserId(activity.getAttributedTo());
            friend2.setFriendId(activity.getCreatorId());
            friend2.setAccepted(true);
            friend2.setBlocked(false);
            friendService.addFriend(friend2);

            Follower follower2 = new Follower();
            follower2.setCreated(new Date());
            follower2.setFollower(activity.getAttributedTo());
            follower2.setFollowee(activity.getCreatorId());
            follower2.setBlocked(false);
            followService.createFollower(follower2);

        }
    }

    @Override
    public Activity getActivity(Long activityId) {
        return store().get(Activity.class, activityId);
    }

    @Override
    public Activity updateActivity(Activity activity) {
        return null;
    }

    @Override
    public boolean deleteActivity(Long activityId) {
        return false;
    }

    @Override
    public List<Activity> listActivities(Long attributedTo) {
        return null;
    }

}
