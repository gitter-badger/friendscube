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
package com.dotweblabs.friendscube.app.client.local.widgets;

import com.dotweblabs.friendscube.app.client.local.util.ClientProxyHelper;
import com.dotweblabs.friendscube.app.client.local.widgets.notifications.NotificationItem;
import com.dotweblabs.friendscube.app.client.shared.ActivityResourceProxy;
import com.dotweblabs.friendscube.app.client.shared.NotificationsResourceProxy;
import com.dotweblabs.friendscube.app.client.shared.entity.Notification;
import com.dotweblabs.friendscube.app.client.shared.entity.Notifications;
import com.dotweblabs.friendscube.app.client.shared.entity.activities.Activity;
import com.dotweblabs.friendscube.app.client.local.*;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.*;
import org.jboss.errai.ui.nav.client.local.TransitionTo;
import org.jboss.errai.ui.shared.ServerTemplateProvider;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.restlet.client.resource.Result;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
/**
 * @author <a href="mailto:kerbymart@gmail.com">Kerby Martino</a>
 * @version 1.0
 * @since 1.0
 */
@Dependent
@Templated(value = "/templates/index.html#navBar", provider = ServerTemplateProvider.class)

public class NavBar extends Composite {

    @Inject
    @DataField
    Anchor messagesLink;

    @Inject
    @DataField
    Anchor notificationsLink;

    @Inject
    @DataField
    Anchor logoutLink;

    @Inject
    @DataField
    Anchor logoLink;

    @Inject
    @DataField
    Anchor homeLink;

    @Inject
    @DataField
    Anchor settingsLink;

    @Inject
    @DataField
    TextBox searchBox;

    @Inject
    @DataField
    Anchor seeAllNotificationsLink;

    @Inject
    TransitionTo<LoginPage> loginPage;

    @Inject
    TransitionTo<MessagesPage> messagesPage;

    @Inject
    TransitionTo<NotificationPage> notificationPage;

    @Inject
    TransitionTo<WelcomePage> welcomePage;

    @Inject
    TransitionTo<SearchPage> searchPage;

    @Inject
    TransitionTo<SettingsPage> settingsPage;

    @Inject
    @DataField
    FlowPanel notificationList;

//    @Inject
//    Instance<SimplePanel> notificationPanel;

    @Inject
    Instance<NotificationItem> notificationItem;

    @Inject
    LoggedInUser loggedInUser;

    private static boolean isNotificationsPopupShown = false;
    private static boolean isMessagesPopupShown = false;

    @PostConstruct
    public void buildUI(){
        searchBox.addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent keyUpEvent) {
                if(keyUpEvent.getNativeKeyCode() == KeyCodes.KEY_ENTER){
                    Multimap<String, String> state = ArrayListMultimap.create();
                    state.put("query", searchBox.getText());
                    searchPage.go(state);
                }
            }
        });
        if(loggedInUser.getUser() != null) {
            loadNotifications();
        }
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
                    NotificationItem item = notificationItem.get();
                    loadNotificationActivity(notification, item);
                }
            }
        });
    }

    private void loadNotificationActivity(Notification notification, final NotificationItem item) {
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
                item.setModel(Activity.ActivityType.valueOf(activity.getType()));
                notificationList.add(item);
            }
        });
    }

    @EventHandler("notificationsLink")
    public void clickNotificationsHandler(ClickEvent event) {
        event.preventDefault();
        isNotificationsPopupShown = !isNotificationsPopupShown;
        if(isMessagesPopupShown){
            fadeToggleMessages();
        }
        fadeToggle();
    }

    @EventHandler("messagesLink")
    public void clickMessagesLinkHandler(ClickEvent event) {
        event.preventDefault();
//        isMessagesPopupShown = !isMessagesPopupShown;
//        if(isNotificationsPopupShown){
//            fadeToggle();
//        }
//        fadeToggleMessages();
        messagesPage.go();
    }

    @EventHandler("seeAllNotificationsLink")
    public void setSeeAllNotifications(ClickEvent event) {
        event.preventDefault();
        isMessagesPopupShown = !isMessagesPopupShown;
        notificationPage.go();
    }

    @EventHandler("logoutLink")
    public void clickLogoutLinkHandler(ClickEvent event) {
        event.preventDefault();
        Multimap<String, String> state = ArrayListMultimap.create();
        state.put("action", "logout");
        //Logger.consoleLog("logging out..");
        loggedInUser.clear();
        loginPage.go(state);
    }

    @EventHandler("logoLink")
         public void logo(ClickEvent event) {
        event.preventDefault();
        welcomePage.go();
    }

    @EventHandler("homeLink")
    public void home(ClickEvent event) {
        event.preventDefault();
        welcomePage.go();
    }

    @EventHandler("settingsLink")
    public void settings(ClickEvent event){
        event.preventDefault();
        settingsPage.go();
    }

    // TODO: Use GwtQuery
    public static native void fadeToggle()/*-{
        $wnd.$("#notificationContainer").fadeToggle(300);
        $wnd.$("#notification_count").fadeOut("slow");
    }-*/;

    // TODO: Use GwtQuery
    public static native void fadeToggleMessages()/*-{
        $wnd.$("#messageNotificationContainer").fadeToggle(300);
        $wnd.$("#messageNotification_count").fadeOut("slow");
    }-*/;
}
