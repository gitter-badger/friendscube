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
package com.dotweblabs.friendscube.app.client.local;

import com.dotweblabs.friendscube.app.client.local.util.ClientProxyHelper;
import com.dotweblabs.friendscube.app.client.local.widgets.notifications.NotificationPageItem;
import com.dotweblabs.friendscube.app.client.shared.ActivityResourceProxy;
import com.dotweblabs.friendscube.app.client.shared.NotificationsResourceProxy;
import com.dotweblabs.friendscube.app.client.shared.entity.Notification;
import com.dotweblabs.friendscube.app.client.shared.entity.Notifications;
import com.dotweblabs.friendscube.app.client.shared.entity.activities.Activity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.dotweblabs.friendscube.app.client.local.widgets.NavBar;
import com.google.gwt.user.client.ui.FlowPanel;
import org.jboss.errai.ui.nav.client.local.Page;
import org.jboss.errai.ui.nav.client.local.PageShown;
import org.jboss.errai.ui.shared.ServerTemplateProvider;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.restlet.client.resource.Result;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

/**
 * @author <a href="mailto:kerbymart@gmail.com">Kerby Martino</a>
 * @version 1.0
 * @since 1.0
 */
@Dependent
@Templated(value = "/templates/NotificationPage.html#notification", provider = ServerTemplateProvider.class)
@Page
public class NotificationPage extends Composite{

    @Inject
    @DataField
    NavBar navBar;

    @Inject
    @DataField
    FlowPanel notifications;

    @Inject
    LoggedInUser loggedInUser;

    @Inject
    Instance<NotificationPageItem> notificationPageItem;

    @PageShown
    public void ready() {
        loadNotifications();
    }

    private void loadNotifications(){
        NotificationsResourceProxy notificationsResourceProxy = GWT.create(NotificationsResourceProxy.class);
        notificationsResourceProxy.getClientResource().setReference(ClientProxyHelper.restRootPath() + NotificationsResourceProxy.NOTIFICATIONS_URI +"/"+loggedInUser.getUser().getId()+"/notifications");
        notificationsResourceProxy.listNotifications(loggedInUser.getUser().getId(), new Result<Notifications>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(Notifications notifications) {
                for(Notification notification :  notifications.getNotifications()) {
                    NotificationPageItem item = notificationPageItem.get();
                    loadNotificationActivity(notification, item);
                }
            }
        });
    }

    private void loadNotificationActivity(Notification notification, final NotificationPageItem item) {
        ActivityResourceProxy activityResourceProxy = GWT.create(ActivityResourceProxy.class);
        activityResourceProxy.getClientResource().setReference(ClientProxyHelper.restRootPath() + ActivityResourceProxy.ACTIVITY_URI + loggedInUser.getUser().getId() + "/activities/" + notification.getReferenceActivityId());
        activityResourceProxy.getActivity(notification.getReferenceActivityId(), new Result<Activity>() {
            @Override
            public void onFailure(Throwable throwable) {
//                item.itemDescription.setText("Error could not load related activity.");
            }

            @Override
            public void onSuccess(Activity activity) {
                item.setActivity(activity);
                notifications.add(item);
            }
        });
    }



}
