package com.dotweblabs.friendscube.app.client.local.widgets.notifications;

import com.dotweblabs.friendscube.app.client.local.LoggedInUser;
import com.dotweblabs.friendscube.app.client.shared.entity.activities.Activity;
import com.google.gwt.user.client.ui.*;
import org.jboss.errai.ui.client.widget.HasModel;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

/**
 * Created by loucar on 5/25/2015.
 */
@Dependent
@Templated
public class NotificationItem extends Composite implements HasModel<Activity.ActivityType>{

    Activity.ActivityType activityType;

    @Inject
    @DataField
    Label itemDescription;

    @Inject
    @DataField
    SimplePanel itemActions;

//    @Inject
//    @DataField
//    Button acceptFriendship;
//
//    @Inject
//    @DataField
//    Button denyFriendship;

    Activity activity;

    @Inject
    LoggedInUser loggedInUser;

    @Inject
    Instance<FriendRequestItem> friendRequestItem;


    @Override
    public Activity.ActivityType getModel() {
        return activityType;
    }

    @Override
    public void setModel(Activity.ActivityType activityType) {
        this.activityType = activityType;
        buildItem();
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        itemDescription.setText(activity.getVerb());
        this.activity = activity;
    }



    private void buildItem() {
        if(Activity.ActivityType.REQUEST.equals(activityType)){
            FriendRequestItem requestItem = friendRequestItem.get();
            requestItem.setActivity(activity);
            itemActions.add(requestItem);
        }
    }
}
