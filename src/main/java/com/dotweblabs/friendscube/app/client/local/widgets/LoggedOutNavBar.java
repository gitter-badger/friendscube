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

import com.dotweblabs.friendscube.app.client.local.LoginPage;
import com.google.gwt.user.client.ui.Composite;
import com.dotweblabs.friendscube.app.client.local.MessagesPage;
import org.jboss.errai.ui.nav.client.local.TransitionTo;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
/**
 * @author <a href="mailto:kerbymart@gmail.com">Kerby Martino</a>
 * @version 1.0
 * @since 1.0
 */
@Dependent
@Templated
public class LoggedOutNavBar extends Composite {

    @Inject
    TransitionTo<LoginPage> loginPage;

    @Inject
    TransitionTo<MessagesPage> messagesPage;

    @PostConstruct
    public void buildUI(){
    }


}
