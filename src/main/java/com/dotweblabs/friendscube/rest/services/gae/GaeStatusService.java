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

import com.dotweblabs.friendscube.app.client.shared.entity.actions.Like;
import com.dotweblabs.friendscube.app.client.shared.entity.actions.Status;
import com.dotweblabs.friendscube.app.client.shared.entity.actions.StatusList;
import com.dotweblabs.friendscube.app.client.shared.entity.activities.Activity;
import com.dotweblabs.friendscube.app.client.shared.entity.relationships.Follower;
import com.dotweblabs.friendscube.app.client.shared.entity.relationships.Followers;
import com.dotweblabs.friendscube.rest.services.ActivityService;
import com.dotweblabs.friendscube.rest.services.FollowService;
import com.dotweblabs.friendscube.rest.services.LikeService;
import com.dotweblabs.friendscube.rest.services.StatusService;
import com.google.inject.Inject;
import com.hunchee.twist.types.ListResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.hunchee.twist.ObjectStoreService.store;

/**
 * @author <a href="mailto:kerbymart@gmail.com">Kerby Martino</a>
 * @version 1.0
 * @since 1.0
 */
public class GaeStatusService implements StatusService {

    @Inject
    LikeService likeService;

    @Inject
    ActivityService activityService;

    @Inject
    FollowService followService;

    @Override
    public void create(Status status) {
        store().put(status);
        Activity activity = new Activity(Activity.ActivityType.POST,
                status.getId(), status.getUserId());
        activityService.createActivity(activity);
    }

    @Override
    public Status read(Long id) {
        return store().get(Status.class, id);
    }


    @Override
    public void update(Status status) {
        Status saved = store().get(Status.class, status.getId());
        saved.setModified(new Date());
        saved.setMessage(status.getMessage());
        store().put(saved);
    }

    @Override
    public void delete(Long id) {
        store().delete(Status.class, id);
    }

    @Override
    public StatusList listStatus(Long userId, String cursor, int limit) {
        StatusList statusList = null;
        Followers followers = followService.listFollowers(userId);
        List<Long> userIds = getFollowerIds(followers);
        userIds.add(userId);
        statusList = getStatusList(cursor, limit, userIds);
        return statusList;
    }

    private StatusList getStatusList(String cursor, int limit, List<Long> userIds) {
        StatusList statusList = new StatusList(new ArrayList<Status>(), null);
        for(Long userId : userIds) {
            if (validateCursor(cursor)) {
                ListResult<Status> result = store().find(Status.class)
                        .equal("userId", userId)
                        .limit(limit)
                        .withCursor(cursor)
                        .sortDescending("created")
                        .asList();
                statusList.getStatuses().addAll(result.getList());
            } else {
                ListResult<Status> result = store().find(Status.class)
                        .equal("userId", userId)
                        .limit(limit)
                        .sortDescending("created")
                        .asList();
                statusList.getStatuses().addAll(result.getList());
            }
        }
        return statusList;
    }

    private List<Long> getFollowerIds(Followers followers) {
        List<Long> ids = new ArrayList<Long>();
        for(Follower follower : followers.getFollowers()){
            ids.add(follower.getFollower());
        }
        return ids;
    }

    @Override
    public void likeStatus(Long statusId, long userId) {
        Like like = new Like();
        like.setUserId(userId);
        like.setItemId(statusId);
        like.setItemType(Like.LikeType.STATUS.name());
        like.setCreated(new Date());
        like.setModified(new Date());
        likeService.create(like);
        Activity activity = new Activity(Activity.ActivityType.LIKED, like.getId(), userId);
        activityService.createActivity(activity);
    }

    @Override
    public void unlikeStatus(Long statusId, long userId) {
        Like like = likeService.readLikeByStatusId(statusId, userId);
        if(like != null){
            Activity activity = new Activity(Activity.ActivityType.UNLIKE, like.getId(), userId);
            likeService.delete(like.getId());
            activityService.createActivity(activity);
        }
    }

    private boolean validateCursor(String cursor){
        if(cursor != null){
            return true;
        }
        return false;
    }
}

