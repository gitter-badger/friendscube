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
package com.dotweblabs.friendscube.app.client.shared;

import com.dotweblabs.friendscube.app.client.shared.entity.actions.Request;
import org.restlet.client.resource.ClientProxy;
import org.restlet.client.resource.Post;
import org.restlet.client.resource.Put;
import org.restlet.client.resource.Result;

/**
 * @author <a href="mailto:kerbymart@gmail.com">Kerby Martino</a>
 * @version 1.0
 * @since 1.0
 */
public interface RequestActionResourceProxy extends ClientProxy {

    static final String REQUESTS_URI = "requests";

    @Post
    public void newRequest(Request request , Result<Void> callback);

    @Put
    public void respondRequest(Request request , Result<Void> callback);


}
