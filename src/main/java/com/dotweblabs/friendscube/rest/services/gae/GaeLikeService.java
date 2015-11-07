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
import com.dotweblabs.friendscube.app.client.shared.entity.actions.Like;
import com.dotweblabs.friendscube.app.client.shared.entity.activities.Activity;
import com.dotweblabs.friendscube.rest.services.ActivityService;
import com.dotweblabs.friendscube.rest.services.LikeService;
import com.dotweblabs.friendscube.rest.services.StatusService;
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
public class GaeLikeService implements LikeService {

    @Inject
    ActivityService activityService;

    @Inject
    UserService userService;

    @Inject
    StatusService statusService;

    @Override
    public Like create(final Like like) {
        Key likeKey = store().transact(new Function<Key>() {
            @Override
            public Key execute() {
                System.out.println("item id: " + like.getItemId());
                Activity activity = new Activity();
                activity.setCreatorId(like.getUserId());
                activity.setType(Activity.ActivityType.LIKED.toString());
                User user = userService.read(like.getUserId());
                activity.setVerb(user.getProfile().getFirstName() + " liked your " + like.getItemType());
                activityService.createActivity(activity);

                Notification notification = new Notification();
                notification.setCreated(new Date());
                notification.setSeen(false);
                notification.setReferenceActivityId(activity.getActivityId());
                if(Like.LikeType.STATUS.equals(Like.LikeType.valueOf(like.getItemType()))){
                    Long userId = statusService.read(like.getItemId()).getUserId();
                    notification.setUserId(userId);
                }
                System.out.println("saving...");
                store().put(notification);
                return store().put(like);
            }
        });

        return like;
    }

    @Override
    public Like read(Long id) {
        return store().get(Like.class, id);
    }

    @Override
    public Like update(Like like) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public List<Like> findLikeByCommentId(Long commentId) {
        return null;
    }

    @Override
    public List<Like> findLikeByStatusId(Long statusId) {
        return null;
    }

    @Override
    public Like readLikeByCommentId(Long commentId, Long userId) {
        return null;
    }

    @Override
    public Like readLikeByStatusId(Long statusId, Long userId) {
        return null;
    }
}

