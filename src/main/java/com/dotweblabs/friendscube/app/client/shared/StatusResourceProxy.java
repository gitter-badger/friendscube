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
/**
 * @author <a href="mailto:kerbymart@gmail.com">Kerby Martino</a>
 * @version 1.0
 * @since 1.0
 */
import org.restlet.client.resource.*;

public interface StatusResourceProxy extends ClientProxy {

    static final String STATUSES_URI = "status";
    @Post
    public void create(com.dotweblabs.friendscube.app.client.shared.entity.actions.Status status, Result<com.dotweblabs.friendscube.app.client.shared.entity.actions.Status> callback);
    @Delete
    public void remove(Result<Void> callback);
}