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
import com.dotweblabs.friendscube.app.client.local.widgets.SearchItemWidget;
import com.dotweblabs.friendscube.app.client.shared.ProfilesResourceProxy;
import com.dotweblabs.friendscube.app.client.shared.entity.search.SearchItem;
import com.dotweblabs.friendscube.app.client.shared.entity.search.SearchItems;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.dotweblabs.friendscube.app.client.local.widgets.NavBar;
import com.google.gwt.user.client.ui.SimplePanel;
import org.jboss.errai.ui.nav.client.local.Page;
import org.jboss.errai.ui.nav.client.local.PageShown;
import org.jboss.errai.ui.nav.client.local.PageState;
import org.jboss.errai.ui.nav.client.local.TransitionTo;
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
@Templated(value = "/SearchPage.html#search", provider = ServerTemplateProvider.class)
@Page
public class SearchPage extends Composite {

    @Inject
    TransitionTo<WelcomePage> welcomePage;

    @Inject
    TransitionTo<LoginPage> loginPage;

    @Inject
    TransitionTo<MessagesPage> messagesPage;

    @Inject
    TransitionTo<UserFeedsPage> userFeeds;

    @Inject
    @DataField
    NavBar navBar;

    @Inject
    @DataField
    SimplePanel searchResults;

    @PageState
    String query;

    @Inject
    Instance<SearchItemWidget> searchItemWidgets;

    @PageShown
    public void ready(){
        ProfilesResourceProxy profilesResourceProxy = GWT.create(ProfilesResourceProxy.class);
        profilesResourceProxy.getClientResource().setReference(ClientProxyHelper.restRootPath() + ProfilesResourceProxy.PROFILES_URI);
        profilesResourceProxy.getClientResource().addQueryParameter("query",query);
        profilesResourceProxy.findProfiles(query, new Result<SearchItems>() {
            @Override
            public void onFailure(Throwable throwable) {

            }
            @Override
            public void onSuccess(SearchItems searchItems) {
                for (SearchItem item : searchItems.getSearchItems()){
                    SearchItemWidget searchWidget = searchItemWidgets.get();
                    searchWidget.setModel(item);
                    searchResults.add(searchWidget);
                }
            }
        });
    }

}