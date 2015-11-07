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
package com.dotweblabs.friendscube.rest.resources.gae;

import com.dotweblabs.friendscube.rest.guice.SelfInjectingServerResource;
import com.dotweblabs.friendscube.rest.resources.MailerResource;
import com.dotweblabs.friendscube.rest.services.EmailService;
import com.dotweblabs.friendscube.rest.utils.URLUtil;
import com.google.inject.Inject;
import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.representation.Representation;

import java.util.logging.Logger;
/**
 * @author <a href="mailto:kerbymart@gmail.com">Kerby Martino</a>
 * @version 1.0
 * @since 1.0
 */
public class GaeMailerServerResource extends SelfInjectingServerResource
        implements MailerResource {

    private static final Logger LOG
            = Logger.getLogger(GaeMailerServerResource.class.getName());

    @Inject
    EmailService emailService;

    @Override
    protected void doInit() {
        super.doInit();
    }

    @Override
    public void sendMail(Representation entity) {

        Form form= new Form(entity);

        String email = form.getFirstValue("email");
        String forgot = form.getFirstValue("forgot");
        String token = form.getFirstValue("token");
        String resend = form.getFirstValue("resend");

        String baseURI = URLUtil.getBasePath();
        LOG.info("Registration: " + "http://" + baseURI + "/registrations?token="+token);

        if(resend != null){
            // Verify (resend)
            emailService.sendEmail(email, "mailer@friendscube.com", "Registration Successful","" +
                    "Hi!, <br/>" +
                    "Thank you for registering. Please click the link to verify your email and be able to login. <br/>" +
                    "Click the link <a href=\"http://" + baseURI + "/registrations?token="+token+"\">here</a> to verify your registration." +
                    "<br/><br/>"+
                    "Regards,<br/>" +
                    "Friends Team");
        }else if(forgot != null){
            emailService.sendEmail(email, "mailer@friendscube.com", "Password Reset","" +
                    "Hi!, <br/>" +
                    "Your request to reset your password has been completed. <br/>" +
                    "Click the link <a href=\"http://" + baseURI + "/registrations?token="+token+"&is_forgot=true\">here</a> to verify your identity and continue to login." +
                    "<br/><br/>"+
                    "Regards,<br/>" +
                    "Friends Team");
        } else if(email != null && token != null){
            // Verify
            emailService.sendEmail(email, "mailer@friendscube.com", "Registration Successful","" +
                    "Hi!, <br/>" +
                    "Thank you for registering. Please click the link to verify your email and be able to login. <br/>" +
                    "Click the link <a href=\"http://" + baseURI + "/registrations?token="+token+"\">here</a> to verify your registration." +
                    "<br/><br/>"+
                    "Regards,<br/>" +
                    "Friends Team");
        } else {
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
        }

    }
}
