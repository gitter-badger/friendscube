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
package com.dotweblabs.friendscube.rest.guice;

import com.dotweblabs.friendscube.rest.services.*;
import com.dotweblabs.friendscube.rest.services.gae.*;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.name.Names;
import org.restlet.Context;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:kerbymart@gmail.com">Kerby Martino</a>
 * @version 1.0
 * @since 1.0
 */
public class GuiceConfigModule extends AbstractModule {

    private static final Logger log = Logger.getLogger(GuiceConfigModule.class.getName());
    private Context context;

    public GuiceConfigModule(){}

    public GuiceConfigModule(Context context) {
        super();
        this.context = context;
    }

    @Override
    protected void configure() {
        // Suppress Guice warning when on GAE
        // see https://code.google.com/p/google-guice/issues/detail?id=488
        Logger.getLogger("com.google.inject.internal.util").setLevel(Level.WARNING);

        Properties configProps = readProperties();
        Names.bindProperties(binder(), configProps);
        bind(WebTokenService.class).to(GaeWebTokenService.class).in(Scopes.SINGLETON);
        bind(UserService.class).to(GaeUserService.class).in(Scopes.SINGLETON);
        bind(ProfileService.class).to(GaeProfileService.class).in(Scopes.SINGLETON);
        bind(RegistrationService.class).to(GaeRegistrationService.class).in(Scopes.SINGLETON);
        bind(StatusService.class).to(GaeStatusService.class).in(Scopes.SINGLETON);
        bind(ActivityService.class).to(GaeActivityService.class).in(Scopes.SINGLETON);
        bind(NotificationService.class).to(GaeNotificationService.class).in(Scopes.SINGLETON);
        bind(LikeService.class).to(GaeLikeService.class).in(Scopes.SINGLETON);
        bind(RequestActionService.class).to(GaeRequestActionService.class).in(Scopes.SINGLETON);
        bind(NotificationService.class).to(GaeNotificationService.class).in(Scopes.SINGLETON);
        bind(ActivityService.class).to(GaeActivityService.class).in(Scopes.SINGLETON);
        bind(FriendService.class).to(GaeFriendService.class).in(Scopes.SINGLETON);
        bind(FollowService.class).to(GaeFollowService.class).in(Scopes.SINGLETON);
        bind(EmailService.class).to(GaeEmailService.class).in(Scopes.SINGLETON);
        bind(MessageService.class).to(GaeMessageService.class).in(Scopes.SINGLETON);
        bind(String.class).annotatedWith(Names.named("app")).toInstance("Friends");
    }

    protected Properties readProperties(){
        if(context != null){
            InputStream is =  context.getClass().getResourceAsStream("/app.properties");
            Properties props = new Properties();
            try {
                props.load(is);
                return props;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
