package com.dotweblabs.friendscube.app.client.local.widgets.notifications;

import com.dotweblabs.friendscube.app.client.local.LoggedInUser;
import com.dotweblabs.friendscube.app.client.local.util.ClientProxyHelper;
import com.dotweblabs.friendscube.app.client.shared.ActivityResourceProxy;
import com.dotweblabs.friendscube.app.client.shared.entity.activities.Activity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.restlet.client.resource.Result;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

/**
 * Created by hehe on 6/2/2015.
 */
@Dependent
@Templated
public class FriendRequestItem extends Composite {

    @Inject
    @DataField
    Button acceptFriendship;

    @Inject
    @DataField
    Button denyFriendship;

    Activity activity;

    @Inject
    LoggedInUser loggedInUser;

    @EventHandler("acceptFriendship")
    public void acceptFriendship(ClickEvent event){
        event.preventDefault();
        Activity activity = new Activity();
        activity.setCreatorId(loggedInUser.getUser().getId());
        activity.setAttributedTo(this.getActivity().getCreatorId());
        activity.setReferenceId(this.getActivity().getActivityId());
        activity.setType(Activity.ActivityType.ACCEPT.toString());

        ActivityResourceProxy activityResourceProxy = GWT.create(ActivityResourceProxy.class);
        activityResourceProxy.getClientResource().setReference(ClientProxyHelper.restRootPath() + ActivityResourceProxy.ACTIVITY_URI + loggedInUser.getUser().getId() + "/activities/" + this.getActivity().getActivityId());
        activityResourceProxy.newActivity(activity, new Result<Void>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(Void aVoid) {
                Window.alert("You accepted this friend request");
            }
        });

    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
