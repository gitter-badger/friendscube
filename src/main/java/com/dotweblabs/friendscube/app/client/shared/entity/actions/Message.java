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

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.hunchee.twist.annotations.Embedded;
import com.hunchee.twist.annotations.Entity;
import com.hunchee.twist.annotations.Id;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author <a href="mailto:kerby@gmail.com">Kerby Martino</a>
 * @version 1.0
 * @since 1.0
 */
@Entity
@XStreamAlias("message")
public class Message implements Serializable {
    @Id
    private Long id;
    private Long from;
    private Long to;
    private String message;
    @XStreamAlias("message_type")
    private String messageType;
    private boolean seen = false;
    private Date created;
    private Date modified;
    // Blob keys of attachments
    @Embedded
    private List<String> attachments;

    public Message(){}

    public Message(Long from, Long to, String message, Date created, Date modified){
        setFrom(from);
        setTo(to);
        setMessage(message);
        setCreated(created);
        setModified(modified);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public Long getTo() {
        return to;
    }

    public void setTo(Long to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public enum MessageType {
        NEW,
        REPLY
    }
}
