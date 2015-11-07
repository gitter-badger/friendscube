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
package com.dotweblabs.friendscube.app.client.local.widgets.like;

import com.dotweblabs.friendscube.app.client.local.util.ClientProxyHelper;
import com.dotweblabs.friendscube.app.client.shared.LikeResourceProxy;
import com.dotweblabs.friendscube.app.client.shared.entity.actions.Like;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.restlet.client.resource.Result;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

/**
 * @author <a href="mailto:kerbymart@gmail.com">Kerby Martino</a>
 * @version 1.0
 * @since 1.0
 */
@Dependent
@Templated
public class LikeButton extends Composite{

    @Inject
    @DataField
    Anchor likeLink;

    private Long userId;

    private Long itemId;

    private String itemType;

    @EventHandler("likeLink")
    public void comment(ClickEvent event){
        event.preventDefault();
        LikeResourceProxy likeResourceProxy = GWT.create(LikeResourceProxy.class);
        likeResourceProxy.getClientResource().setReference(ClientProxyHelper.restRootPath() + LikeResourceProxy.LIKES_URI);
        Like like = new Like();
        like.setUserId(userId);
        like.setItemId(itemId);
        like.setItemType(itemType);
        likeResourceProxy.store(like, new Result<Void>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(Void aVoid) {

            }
        });

    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }
}
