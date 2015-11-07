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
package com.dotweblabs.friendscube.app.client.shared.entity.relationships;

import java.io.Serializable;
import java.util.List;
/**
 * @author <a href="mailto:kerbymart@gmail.com">Kerby Martino</a>
 * @version 1.0
 * @since 1.0
 */
public class Followers implements Serializable {
    private List<Follower> followers;
    private String cursor;
    private Long count;

    public Followers(){}

    public Followers(List<Follower> followers, String cursor, Long count){
        setFollowers(followers);
        setCursor(cursor);
        setCount(count);
    }


    public List<Follower> getFollowers() {
        return followers;
    }

    public void setFollowers(List<Follower> followers) {
        this.followers = followers;
    }

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
