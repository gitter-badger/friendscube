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

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.google.gwt.user.client.ui.*;
import org.jboss.errai.ioc.client.api.EntryPoint;
import org.jboss.errai.ui.nav.client.local.Navigation;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.jboss.errai.ui.shared.ServerTemplateProvider;

/**
 * @author <a href="mailto:kerbymart@gmail.com">Kerby Martino</a>
 * @version 1.0
 * @since 1.0
 */
@Templated(value = "/Bootstrap.html#main", provider = ServerTemplateProvider.class)
@ApplicationScoped
@EntryPoint
public class Bootstrap extends Composite {

    @Inject
    Navigation navigation;

    @Inject @DataField
    private SimplePanel content;

    @Inject
    LoggedInUser loggedInUser;
    
    @PostConstruct
    public void buildUI() {
        //GWT.setUncaughtExceptionHandler(new SuperDevModeUncaughtExceptionHandler());
        content.add(navigation.getContentPanel());
        RootPanel.get("rootPanel").add(this);
    }
}
