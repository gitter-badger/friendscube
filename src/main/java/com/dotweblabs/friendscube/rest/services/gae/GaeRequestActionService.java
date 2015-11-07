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
import com.dotweblabs.friendscube.app.client.shared.entity.actions.Request;
import com.dotweblabs.friendscube.app.client.shared.entity.activities.Activity;
import com.dotweblabs.friendscube.rest.services.ActivityService;
import com.dotweblabs.friendscube.rest.services.NotificationService;
import com.dotweblabs.friendscube.rest.services.RequestActionService;
import com.dotweblabs.friendscube.rest.services.UserService;
import com.google.appengine.api.datastore.Key;
import com.google.inject.Inject;
import com.hunchee.twist.types.Function;

import java.util.Date;

import static com.hunchee.twist.ObjectStoreService.store;

/**
 * @author <a href="mailto:kerbymart@gmail.com">Kerby Martino</a>
 * @version 1.0
 * @since 1.0
 */
public class GaeRequestActionService implements RequestActionService {

    @Inject
    NotificationService notificationService;

    @Inject
    ActivityService activityService;

    @Inject
    UserService userService;

    @Override
    public void create(final Request request) {
        store().transact(new Function<Key>() {
            @Override
            public Key execute() {
                request.setCreated(new Date());

                Activity activity = new Activity();
                activity.setAttributedTo(request.getRequesterId());
                User user = userService.read(request.getRequesterId());
                if(Request.RequestType.FRIENDSHIP.toString().equals(request.getType())){
                    activity.setVerb(user.getProfile().getFirstName() + " requested to be your friend.");
                    activity.setType(Activity.ActivityType.REQUEST.toString());
                }

                store().put(activity);

                Notification notification = new Notification();
                notification.setCreated(new Date());
                notification.setUserId(request.getRequesteeId());
                notification.setSeen(false);
                notification.setReferenceActivityId(activity.getActivityId());

                store().put(notification);
                return store().put(request);
            }
        });
    }
}
