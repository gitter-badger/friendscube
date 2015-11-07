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
@ApiModel
@XStreamAlias("registration")
public class Registration implements Serializable {
    @Id
    @ApiModelProperty(position = 1, required = true, value = "user email containing only lowercase letters")
    private String email;
    @XStreamAlias("firstname")
    private String firstName;
    @XStreamAlias("middlename")
    private String middleName;
    @XStreamAlias("lastname")
    private String lastName;
    @XStreamAlias("birthdate")
    private Date birthDate;
    private boolean gender;
    private String password;
    @ApiModelProperty(hidden = true)
    @XStreamOmitField
    private Date created;
    @ApiModelProperty(hidden = true)
    @XStreamOmitField
    private Date modified;
    @ApiModelProperty(value = "Expiration of this registration")
    private Date expiration;
    @Volatile
    private String token;

    public Registration(){}

    public Registration(String email, String password){
        setEmail(email);
        setPassword(password);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "Registration " + "id=" + this.email + ","
                + "id=" + this.email + ","
                + "password=" + this.password + ","
                + "firstname=" + this.firstName + ","
                + "middlename=" + this.middleName + ","
                + "lastname=" + this.lastName + ","
                + "birthdate=" + this.birthDate + ","
                + "gender=" + this.gender;


    }
}
