package org.ingini.jactor.chat.service;

import com.google.common.base.Objects;
import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.Mailbox;
import org.ingini.jactor.chat.actor.ChatActor;
import org.ingini.jactor.chat.domain.UserId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.concurrent.ThreadSafe;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Copyright (c) 2012 Ivan Hristov
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@Service
@ThreadSafe
public class JActorService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ConcurrentMap<UserId, ChatActor> actorMap = new ConcurrentHashMap<UserId, ChatActor>();

    @Autowired
    private Mailbox chatMailbox;

    @Autowired
    private Actor storageActor;

    public ChatActor findOrInit(UserId id) {

        ChatActor clientActor;
        if (!actorMap.containsKey(id)) {
            clientActor = new ChatActor(id, storageActor);

            try {
                clientActor.initialize(chatMailbox);
            } catch (Exception e) {
                logger.error("Problem while initializing actor {}", id, e);
                throw new IllegalStateException(e);
            }
            return Objects.firstNonNull(actorMap.putIfAbsent(id, clientActor), clientActor);
        }

        return actorMap.get(id);
    }

}
