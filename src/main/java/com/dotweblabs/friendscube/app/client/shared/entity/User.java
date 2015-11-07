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

import com.hunchee.twist.annotations.Child;
import com.hunchee.twist.annotations.Entity;
import com.hunchee.twist.annotations.Id;
import com.hunchee.twist.annotations.Volatile;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * @author <a href="mailto:kerby@gmail.com">Kerby Martino</a>
 * @version 1.0
 * @since 1.0
 */
@Entity
@XStreamAlias("user")
@ApiModel
public class User implements Serializable {

    @Id
    @ApiModelProperty(position = 0, required = false, value = "Auto-generated id")
    private Long id;
    @XStreamOmitField
    @ApiModelProperty(required = true, value = "Username or email depending on the domain")
    private String username;
    @XStreamOmitField
    @ApiModelProperty(required = true, value = "Domain used to register this User, e.g. facebook, personal or local")
    private String domain;
    @XStreamOmitField
    @ApiModelProperty(required = true, value = "Base64-encoded password")
    private String passwordHash;
    @ApiModelProperty(required = false, value = "Client token generated from the verification process")
    @Volatile
    @XStreamAlias("client_token")
    private String clientToken;
    @ApiModelProperty(hidden = true)
    @XStreamOmitField
    private Date created;
    @ApiModelProperty(hidden = true)
    @XStreamOmitField
    private Date modified;

    @Child
    private Profile profile;

    public User(){}

    public User(String username, String passwordHash, String domain){
        this.username = username;
        this.passwordHash = passwordHash;
        this.domain = domain;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientToken() {
        return clientToken;
    }

    public void setClientToken(String clientToken) {
        this.clientToken = clientToken;
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

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
