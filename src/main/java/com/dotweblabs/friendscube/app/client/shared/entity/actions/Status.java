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
package com.dotweblabs.friendscube.app.client.shared.entity.actions;

import com.hunchee.twist.annotations.Entity;
import com.hunchee.twist.annotations.Id;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * @author <a href="mailto:kerby@gmail.com">Kerby Martino</a>
 * @version 1.0
 * @since 1.0
 */
@Entity
@XStreamAlias("status")
public class Status implements Serializable {
    @Id
    private Long id;
    @XStreamAlias("user_id")
    private Long userId;
    @ApiModelProperty(value = "Reference status ID or null")
    @XStreamAlias("shared_status_id")
    private Long referenceStatusId;
    private String message;
    private Date created;
    private Date modified;

    // Blob-key
    private String attachment;

    public Status(){}

    public Status(Long id, String message){
        this.id = id;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public Long getReferenceStatusId() {
        return referenceStatusId;
    }

    public void setReferenceStatusId(Long referenceStatusId) {
        this.referenceStatusId = referenceStatusId;
    }
}
