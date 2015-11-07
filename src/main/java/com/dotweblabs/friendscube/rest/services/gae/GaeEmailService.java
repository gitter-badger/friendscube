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
package com.dotweblabs.friendscube.rest.services.gae;

import com.dotweblabs.friendscube.rest.services.EmailService;
import com.postmark.PostmarkMailSender;
import com.postmark.PostmarkMessage;

/**
 * Created by hehe on 6/9/2015.
 */
public class GaeEmailService implements EmailService{

    @Override
    public void sendEmail(String to, String from, String subj, String body){
        PostmarkMailSender mailSender = new PostmarkMailSender("{insert_your_postmark_token_here}");
        PostmarkMessage message = new PostmarkMessage();
        message.setTo(to);
        message.setFrom(from);
        message.setSubject(subj);
        message.setHtmlBody(body);
        mailSender.send(message);
        System.out.println("Email Sent!");
    }

}
