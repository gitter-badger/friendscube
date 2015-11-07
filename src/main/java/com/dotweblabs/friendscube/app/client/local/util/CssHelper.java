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
package com.dotweblabs.friendscube.app.client.local.util;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.LinkElement;
import java.util.LinkedHashMap;
import java.util.Map;
/**
 * @author <a href="mailto:kerbymart@gmail.com">Kerby Martino</a>
 * @version 1.0
 * @since 1.0
 */
public class CssHelper {

    private static Map<String, LinkElement> styles = new LinkedHashMap<String, LinkElement>();

    /** Load CSS file from url */
    public static void loadCss(String url){
        LinkElement link = Document.get().createLinkElement();
        link.setRel("stylesheet");
        link.setHref(url);
        nativeAttachToHead(link);
        styles.put(url, link);
    }

    public static void unloadCss(String url){
        LinkElement scriptElement = styles.get(url);
        if(scriptElement != null){
            scriptElement.removeFromParent();
        }
    }

    /**
     * Attach element to head
     */
    protected static native void nativeAttachToHead(JavaScriptObject scriptElement) /*-{
        $doc.getElementsByTagName("head")[0].appendChild(scriptElement);
    }-*/;

}
