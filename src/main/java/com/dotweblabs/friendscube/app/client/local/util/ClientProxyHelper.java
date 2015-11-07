/*
*
* Copyright (c) 2015 Kerby Martino and Dotweblabs Web Technologies. All Rights Reserved.
* Licensed under Dotweblabs Commercial License, Version 1.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*   http://www.weblabs.ph/licenses/LICENSE-1.0
*
* Unless required by applicable law or agreed to in writing, software distributed
* under the License is distributed as Proprietary and Confidential to
* Dotweblabs Web Technologies and must not be redistributed in any form.
*
*/
package com.dotweblabs.friendscube.app.client.local.util;

import com.dotweblabs.friendscube.app.client.local.resources.ClientProxyConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;

/**
 * @author <a href="mailto:kerbymart@gmail.com">Kerby Martino</a>
 * @version 1.0
 * @since 1.0
 */
public class ClientProxyHelper {
    static ClientProxyConstants clientProxyConstants = GWT.create(ClientProxyConstants.class);
    public static String restRootPath(){
        if(Window.Location.getHost().contains("localhost")){
            return clientProxyConstants.devRestRoot();
        }else {
            return clientProxyConstants.prodRestRoot();
        }
    }
    public static String restPastesPath(){
        return restRootPath() + "/";
    }

}
