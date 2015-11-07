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

import com.dotweblabs.friendscube.app.client.shared.entity.actions.Message;
import com.dotweblabs.friendscube.app.client.shared.entity.actions.MessagesList;
import com.dotweblabs.friendscube.app.client.shared.entity.activities.Activity;
import com.dotweblabs.friendscube.rest.services.ActivityService;
import com.dotweblabs.friendscube.rest.services.MessageService;
import com.google.inject.Inject;
import com.hunchee.twist.types.ListResult;

import java.util.Date;

import static com.hunchee.twist.ObjectStoreService.store;

/**
 * @author <a href="mailto:kerbymart@gmail.com">Kerby Martino</a>
 * @version 1.0
 * @since 1.0
 */
public class GaeMessageService implements MessageService {

    public GaeMessageService(){}

    @Inject
    ActivityService activityService;

    @Override
    public void create(Message message) {
        message.setCreated(new Date());
        store().put(message);
        System.out.println("type: " + message.getMessageType());
       if(message.getMessageType().equals(Message.MessageType.REPLY)){
            Long attributedTo = message.getTo();
            Activity activity = new Activity(Activity.ActivityType.REPLY, message.getId(), attributedTo);
            activityService.createActivity(activity);
        }
    }

    @Override
    public Message read(Long id) {
        return store().get(Message.class, id);
    }

    @Override
    public MessagesList listMessage(Long from, Long to, String cursor, int limit) {
        MessagesList messagesList = null;
        if(validateCursor(cursor)){
            ListResult<Message> result = store().find(Message.class)
                    .equal("from", from).equal("to", to)
                    .limit(limit)
                    .withCursor(cursor)
                    .asList();
            messagesList = new MessagesList(result.getList(), result.getCursor().getWebSafeString());

            ListResult<Message> result1 = store().find(Message.class)
                    .equal("from", to).equal("to", from)
                    .limit(limit)
                    .withCursor(cursor)
                    .asList();
            messagesList.getMessages().addAll(result1.getList());
        } else {
            ListResult<Message> result = store().find(Message.class)
                    .equal("from", from).equal("to", to)
                    .limit(limit)
                    .withCursor(cursor)
                    .asList();
            messagesList = new MessagesList(result.getList(), null);

            ListResult<Message> result1 = store().find(Message.class)
                    .equal("from", to).equal("to", from)
                    .limit(limit)
                    .withCursor(cursor)
                    .asList();
            messagesList.getMessages().addAll(result1.getList());
        }
        return messagesList;

    }


    @Override
    public void update(Message message) {

        // TODO
    }

    @Override
    public void delete(Long id) {
        store().delete(Message.class, id);
    }

    @Override
    public void markAsSeen(Long messageId) {
        Message message = store().get(Message.class, messageId);
        message.setSeen(true);
        message.setModified(new Date());
        store().put(message);
    }
    private boolean validateCursor(String cursor){
        if(cursor != null){
            return true;
        }
        return false;
    }
}

