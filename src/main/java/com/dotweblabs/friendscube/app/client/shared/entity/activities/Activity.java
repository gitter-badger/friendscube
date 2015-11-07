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
package com.dotweblabs.friendscube.app.client.shared.entity.activities;

import com.hunchee.twist.annotations.Entity;
import com.hunchee.twist.annotations.Id;

@Entity
public class Activity extends AbstractActivity {

    @Id
    private Long activityId;
    private Long referenceId;
    private Long attributedTo;
    private Long creatorId;
    private String verb;
    private String type;

    public Activity() {}

    public Activity(String verb, Long referenceId, Long attributedTo){
        setVerb(verb);
        setReferenceId(referenceId);
    }

    public Activity(ActivityType verb, Long referenceId, Long attributedTo){
        setVerb(verb.name());
        setReferenceId(referenceId);
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Long getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Long referenceId) {
        this.referenceId = referenceId;
    }

    public String getVerb() {
        return verb;
    }

    public void setVerb(String verb) {
        this.verb = verb;
    }

    public Long getAttributedTo() {
        return attributedTo;
    }

    public void setAttributedTo(Long attributedTo) {
        this.attributedTo = attributedTo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }


    public enum ActivityType {
        POST,
        MENTION,
        UPDATE,
        SEND,
        SEE,
        REPLY,
        REQUEST,
        CANCEL,
        ACCEPT,
        DENY,
        FOLLOW,
        UNFOLLOW,
        LIKED,
        UNLIKE,
    }

}
