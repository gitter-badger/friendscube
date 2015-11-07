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
package com.dotweblabs.friendscube.app.client.shared.entity;

import com.hunchee.twist.annotations.Entity;
import com.hunchee.twist.annotations.Id;
import com.hunchee.twist.annotations.Volatile;

import java.io.Serializable;
import java.util.Date;

/**
 * @author <a href="mailto:kerby@gmail.com">Kerby Martino</a>
 * @version 1.0
 * @since 1.0
 */
@Entity
public class Notification implements Serializable {
    @Id
    private Long id;
    private Long userId;
    private Long referenceActivityId;
    @Volatile
    private String createdPretty;
    @Volatile
    private String modifiedPretty;
    private boolean seen;
    private Date created;
    private Date modified;

    public Notification(){}

    public Notification(Long userId, Long referenceActivityId) {
        setUserId(userId);
        setReferenceActivityId(referenceActivityId);
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReferenceActivityId() {
        return referenceActivityId;
    }

    public void setReferenceActivityId(Long referenceActivityId) {
        this.referenceActivityId = referenceActivityId;
    }

    public String getCreatedPretty() {
        return createdPretty;
    }

    public void setCreatedPretty(String createdPretty) {
        this.createdPretty = createdPretty;
    }

    public String getModifiedPretty() {
        return modifiedPretty;
    }

    public void setModifiedPretty(String modifiedPretty) {
        this.modifiedPretty = modifiedPretty;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
