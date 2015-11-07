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
import com.dotweblabs.friendscube.app.client.shared.entity.Notifications;
import com.dotweblabs.friendscube.rest.services.NotificationService;
import com.dotweblabs.friendscube.rest.services.ProfileService;
import com.google.inject.Inject;
import com.hunchee.twist.types.ListResult;
import static com.hunchee.twist.ObjectStoreService.store;

public class GaeNotificationService implements NotificationService {

    @Inject
    ProfileService profileService;

    @Override
    public Notification createNotification(Notification notification) {
        return null;
    }

    @Override
    public Notification updateNotification(Notification notification) {
        return null;
    }

    @Override
    public Notification readNotification(Notification notification) {
        return null;
    }

    @Override
    public boolean deleteNotification(Long notificationId) {
        return false;
    }

    @Override
    public Notifications listNotifications(Long userId) {
        ListResult<Notification> notificationList = store().find(Notification.class)
                .withCursor("")
                .equal("userId", userId)
                .limit(3)
                .asList();

        Notifications notifications = new Notifications();
        notifications.setNotifications(notificationList.getList());
        notifications.setCount(Integer.valueOf(notificationList.getList().size()).longValue());

        return notifications;
    }
}
