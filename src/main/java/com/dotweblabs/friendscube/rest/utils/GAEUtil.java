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
package com.dotweblabs.friendscube.rest.utils;

import com.google.appengine.api.utils.SystemProperty;
/**
 * @author <a href="mailto:kerbymart@gmail.com">Kerby Martino</a>
 * @version 1.0
 * @since 1.0
 */
public class GAEUtil {
    public static boolean isGaeProd() {
        return System.getProperty("com.google.appengine.runtime.environment")
                == SystemProperty.Environment.Value.Production.value();
    }
    public static boolean isGaeDev() {
        return System.getProperty("com.google.appengine.runtime.environment")
                == SystemProperty.Environment.Value.Development.value();
    }
    public static boolean isGaeMode() {
        return System.getProperty("com.google.appengine.runtime.environment") != null;
    }
}
