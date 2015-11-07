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
package com.dotweblabs.friendscube.rest.resources;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Header;
import org.restlet.data.Method;
import org.restlet.engine.header.HeaderConstants;
import org.restlet.representation.EmptyRepresentation;
import org.restlet.routing.Filter;
import org.restlet.util.Series;

/**
 * @author <a href="mailto:kerbymart@gmail.com">Kerby Martino</a>
 * @version 1.0
 * @since 1.0
 */
public class OriginFilter extends Filter {

    public OriginFilter(Context context) {
        super(context);
    }

    @Override
    protected int beforeHandle(Request request, Response response) {
        if(Method.OPTIONS.equals(request.getMethod())) {
            Series<Header> requestHeaders = (Series<Header>) request.getAttributes().get(HeaderConstants.ATTRIBUTE_HEADERS);
            String origin = requestHeaders.getFirstValue("Origin", false, "*");
            String rh = requestHeaders.getFirstValue("Access-Control-Request-Headers", false, "*");
            Series<Header> responseHeaders = (Series<Header>) response
                    .getAttributes().get(HeaderConstants.ATTRIBUTE_HEADERS);
            if (responseHeaders == null) {
                responseHeaders = new Series<Header>(Header.class);
            }
            responseHeaders.add("Access-Control-Allow-Origin", origin);
            responseHeaders.set("Access-Control-Expose-Headers", "Authorization, Link");
            responseHeaders.add("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
            responseHeaders.add("Access-Control-Allow-Headers", rh);
            responseHeaders.add("Access-Control-Allow-Credentials", "true");
            responseHeaders.add("Access-Control-Max-Age", "60");
            response.getAttributes().put(HeaderConstants.ATTRIBUTE_HEADERS, responseHeaders);
            response.setEntity(new EmptyRepresentation());
            return SKIP;
        }

        return super.beforeHandle(request, response);
    }

    @Override
    protected void afterHandle(Request request, Response response) {
        if(!Method.OPTIONS.equals(request.getMethod())) {
            Series<Header> requestHeaders = (Series<Header>) request.getAttributes().get(HeaderConstants.ATTRIBUTE_HEADERS);
            String origin = requestHeaders.getFirstValue("Origin", false, "*");
            String rh = requestHeaders.getFirstValue("Access-Control-Request-Headers", false, "*");
            Series<Header> responseHeaders = (Series<Header>) response
                    .getAttributes().get(HeaderConstants.ATTRIBUTE_HEADERS);
            if (responseHeaders == null) {
                responseHeaders = new Series<Header>(Header.class);
            }
            responseHeaders.add("Access-Control-Allow-Origin", origin);
            responseHeaders.add("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
            responseHeaders.set("Access-Control-Expose-Headers", "Authorization, Link");
            responseHeaders.add("Access-Control-Allow-Headers", rh);
            responseHeaders.add("Access-Control-Allow-Credentials", "true");
            responseHeaders.add("Access-Control-Max-Age", "60");
            response.getAttributes().put(HeaderConstants.ATTRIBUTE_HEADERS,responseHeaders);
        }

        super.afterHandle(request, response);
    }
}
