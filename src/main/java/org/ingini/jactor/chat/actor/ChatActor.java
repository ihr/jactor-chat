package org.ingini.jactor.chat.actor;

import com.google.common.collect.Maps;
import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.ingini.jactor.chat.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.TimeUnit;


/*
* Copyright (c) 2012 Ivan Hristov
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
public class ChatActor extends JLPCActor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserId id;

    private final Map<MessageId, ChatMsg> messages = Maps.newHashMap();

    private final Actor storageActor;

    public ChatActor(UserId id, Actor storageActor) {
        this.id = id;
        this.storageActor = storageActor;
    }

    public void process(ChatMsg req) throws Exception {
        TimeUnit.SECONDS.sleep(2);
        logger.info("Actor {} received new msg ({}) from " + req.getSender(), id, req.getContent());

        messages.put(req.getId(), req);
    }

    /**
     * synchronous method
     *
     * @param req
     * @return
     * @throws Exception
     */
    public Confirmation get(MsgConfirmation req) throws Exception {
        logger.info("Confirming message {}", req.getMessageId());

        logger.info("Start sleeping");
        TimeUnit.SECONDS.sleep(2);
        logger.info("Stop sleeping");
        return messages.containsKey(req.getMessageId()) ? Confirmation.YES : Confirmation.NO;
    }

    public void persist(MessageId messageId) throws Exception {
        PersistChatMsg persistChatMsg = new PersistChatMsg(messages.get(messageId));
        persistChatMsg.send(this, storageActor, new RP<MessageId>() {
            @Override
            public void processResponse(MessageId messageId) throws Exception {
                logger.info("Removing message {}", messageId);
                messages.remove(messageId);
            }
        });
    }
}
