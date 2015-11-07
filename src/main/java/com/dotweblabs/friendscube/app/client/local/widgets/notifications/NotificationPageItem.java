package com.dotweblabs.friendscube.app.client.local.widgets.notifications;

import com.dotweblabs.friendscube.app.client.shared.entity.activities.Activity;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import org.jboss.errai.ui.nav.client.local.PageShown;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

/**
 * Created by hehe on 5/27/2015.
 *
 */
@Dependent
@Templated
public class NotificationPageItem extends Composite{

    @DataField
    @Inject
    Image profilePic;

    @DataField
    @Inject
    Label activityVerb;

    private Activity activity;

    @PageShown
    public void ready(){
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
        activityVerb.setText(activity.getVerb());
    }
}
