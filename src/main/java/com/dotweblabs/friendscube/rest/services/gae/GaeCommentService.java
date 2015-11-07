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

import com.dotweblabs.friendscube.app.client.shared.entity.actions.Comment;
import com.dotweblabs.friendscube.app.client.shared.entity.actions.Like;
import com.dotweblabs.friendscube.app.client.shared.entity.activities.Activity;
import com.dotweblabs.friendscube.rest.services.ActivityService;
import com.dotweblabs.friendscube.rest.services.CommentService;
import com.dotweblabs.friendscube.rest.services.LikeService;
import com.google.inject.Inject;


import java.util.Date;

import static com.hunchee.twist.ObjectStoreService.store;

/**
 * @author <a href="mailto:kerbymart@gmail.com">Kerby Martino</a>
 * @version 1.0
 * @since 1.0
 */
public class GaeCommentService implements CommentService {

    @Inject
    LikeService likeService;

    @Inject
    ActivityService activityService;

    @Override
    public void create(Comment comment) {
        store().put(comment);
    }

    @Override
    public Comment read(Long id) {
        return store().get(Comment.class, id);
    }

    @Override
    public void update(Comment comment) {
        Comment saved = store().get(Comment.class, comment.getId());
        saved.setComment(comment.getComment());
        saved.setModified(new Date());
        store().put(saved);
    }

    @Override
    public void delete(Long id) {
        store().delete(Comment.class, id);
    }

    @Override
    public void likeComment(Long commentId, long userId) {
        Like like = new Like();
        like.setUserId(userId);
        like.setItemId(commentId);
        like.setItemType(Like.LikeType.COMMENT.name());
        like.setCreated(new Date());
        like.setModified(new Date());
        likeService.create(like);
        Activity activity = new Activity(Activity.ActivityType.LIKED, like.getId(), userId);
        activityService.createActivity(activity);
    }

    @Override
    public void unlikeComment(Long commentId, long userId) {
        Like like = likeService.readLikeByCommentId(commentId, userId);
        if(like != null){
            Activity activity = new Activity(Activity.ActivityType.UNLIKE, like.getId(), userId);
            likeService.delete(like.getId());
            activityService.createActivity(activity);
        }
    }
}
