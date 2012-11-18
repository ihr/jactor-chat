package org.ingini.jactor.chat.conf;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.JAMailboxFactory;
import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.MailboxFactory;
import org.ingini.jactor.chat.actor.StorageActor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

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
@Configuration
@ComponentScan({"org.ingini.jactor.chat.conf", "org.ingini.jactor.chat.service", "org.ingini.jactor.chat.actor", "org.ingini.jactor.chat.domain" })
public class JActorConfig {

    @Autowired
    private MailboxFactory messageMailboxFactory;

    @Bean
    public MailboxFactory getMessagesMailboxFactory() {
        double blockingCoefficient = 0.2D; // Almost non-blocking Chat processing logic
        return JAMailboxFactory.newMailboxFactory(
                Math.round((float) (Runtime.getRuntime().availableProcessors() / (1 - blockingCoefficient))));
    }

    @Bean
    public Mailbox chatMailbox() {
        return messageMailboxFactory.createMailbox();
    }

    @Bean
    public Actor storageActor() throws Exception {
        StorageActor storageActor = new StorageActor();
        storageActor.initialize(messageMailboxFactory.createMailbox());
        return storageActor;
    }
}
