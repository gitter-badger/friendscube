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
package com.dotweblabs.friendscube.rest;

import com.dotweblabs.friendscube.rest.resources.OriginFilter;
import com.dotweblabs.friendscube.rest.resources.admin.gae.GaeRegistrationsServerResource;
import com.dotweblabs.friendscube.rest.resources.gae.*;
import com.dotweblabs.friendscube.rest.utils.GAEUtil;
import com.dotweblabs.friendscube.rest.utils.TestData;
import com.google.appengine.api.datastore.Key;
import com.google.inject.Guice;
import com.dotweblabs.friendscube.rest.guice.GuiceConfigModule;
import com.dotweblabs.friendscube.rest.guice.SelfInjectingServerResourceModule;
import org.restlet.Restlet;
import org.restlet.engine.Engine;
import org.restlet.engine.converter.ConverterHelper;
import org.restlet.ext.jackson.JacksonConverter;
import org.restlet.ext.swagger.SwaggerApplication;
import org.restlet.routing.Router;

import static com.hunchee.twist.ObjectStoreService.store;

import java.util.List;

/**
 * @author <a href="mailto:kerbymart@gmail.com">Kerby Martino</a>
 * @version 1.0
 * @since 1.0
 */
public class FriendscubeApplication extends SwaggerApplication {

    private static final String ROOT_URI = "/";

    @Override
    public Restlet createInboundRoot() {
        Guice.createInjector(new GuiceConfigModule(this.getContext()),
                new SelfInjectingServerResourceModule());
        createTestData();
        configureConverters();
        //OriginFilter originFilter = new OriginFilter(getContext());
        Router router = new Router(getContext());
        router.attachDefault(GaeRootServerResource.class);
        // TODO
        router.attach(ROOT_URI + "admin/registrations", GaeRegistrationsServerResource.class);
        router.attach(ROOT_URI + "admin/registrations/", GaeRegistrationsServerResource.class);
        router.attach(ROOT_URI + "registrations", GaeRegistrationServerResource.class);
        router.attach(ROOT_URI + "registrations/", GaeRegistrationServerResource.class);
        router.attach(ROOT_URI + "registrations/emails", GaeMailerServerResource.class);
        router.attach(ROOT_URI + "registrations/emails/", GaeMailerServerResource.class);
        router.attach(ROOT_URI + "tokens", GaeTokensServerResource.class);
        router.attach(ROOT_URI + "tokens/", GaeTokensServerResource.class);
        router.attach(ROOT_URI + "users", GaeUserServerResource.class);
        router.attach(ROOT_URI + "users/", GaeUserServerResource.class);
        router.attach(ROOT_URI + "users/{user_id}", GaeUserServerResource.class);
        router.attach(ROOT_URI + "users/{user_id}/requests", GaeRequestActionServerResource.class);
        router.attach(ROOT_URI + "users/{user_id}/requests/{request_id}", GaeRequestActionServerResource.class);
        router.attach(ROOT_URI + "users/{user_id}/notifications", GaeNotificationsServerResource.class);
        router.attach(ROOT_URI + "users/{user_id}/activities", GaeActivityServerResource.class);
        router.attach(ROOT_URI + "users/{user_id}/activities/{activity_id}", GaeActivityServerResource.class);
        router.attach(ROOT_URI + "users/{user_id}/friends", GaeFriendsServerResource.class);
        router.attach(ROOT_URI + "users/{user_id}/friends/{friend_id}", GaeFriendServerResource.class);
        router.attach(ROOT_URI + "users/{user_id}/followers", GaeFollowersServerResource.class);
        router.attach(ROOT_URI + "users/{user_id}/followers/{follower_id}", GaeFollowerServerResource.class);
        router.attach(ROOT_URI + "profiles", GaeUserProfilesServerResource.class);
        router.attach(ROOT_URI + "profiles/", GaeUserProfileServerResource.class);
        router.attach(ROOT_URI + "profiles/{profile_id}", GaeUserProfileServerResource.class);
        router.attach(ROOT_URI + "status", GaeStatusesServerResource.class);
        router.attach(ROOT_URI + "status/", GaeStatusServerResource.class);
        router.attach(ROOT_URI + "status/{status_id}", GaeStatusServerResource.class);
        router.attach(ROOT_URI + "files", GaeFileServerResource.class);
        router.attach(ROOT_URI + "files/", GaeFileServerResource.class);
        router.attach(ROOT_URI + "requests", GaeRequestActionServerResource.class);
        router.attach(ROOT_URI + "requests/", GaeRequestActionServerResource.class);
        router.attach(ROOT_URI + "messages", GaeMessagesServerResource.class);
        router.attach(ROOT_URI + "messages/", GaeMessagesServerResource.class);
        router.attach(ROOT_URI + "messages/{message_id}", GaeMessageServerResource.class);
        router.attach(ROOT_URI + "messages/{message_id}/", GaeMessageServerResource.class);
        router.attach(ROOT_URI + "likes/", GaeLikeServerResource.class);
        router.attach(ROOT_URI + "likes", GaeLikeServerResource.class);

        attachSwaggerSpecificationRestlet(router, ROOT_URI + "api-docs");

        //originFilter.setNext(router);

        return router;
    }

    private void configureConverters() {
        List<ConverterHelper> converters = Engine.getInstance()
                .getRegisteredConverters();
        JacksonConverter jacksonConverter = null;
        for (ConverterHelper converterHelper : converters) {
            System.err.println(converterHelper.getClass());
            if (converterHelper instanceof JacksonConverter) {
                jacksonConverter = (JacksonConverter) converterHelper;
                break;
            }
        }
        if (jacksonConverter != null) {
            Engine.getInstance()
                    .getRegisteredConverters().remove(jacksonConverter);
        }
    }

    private void createTestData(){
        if(GAEUtil.isGaeDev()){
            Key key = store().put(TestData.createDemoUser("demo@friendscube.xyz"));
            Key friendKey = store().put(TestData.createDemoUser("demofriend@friendscube.xyz"));
            store().put(TestData.createDemoFriendship(key.getId(), friendKey.getId()));
        }
    }
}

