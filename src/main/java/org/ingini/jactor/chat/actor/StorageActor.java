package org.ingini.jactor.chat.actor;

import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.ingini.jactor.chat.domain.PersistChatMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class StorageActor extends JLPCActor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    public void process(PersistChatMsg persistChatMsg, RP rp) throws Exception {
        TimeUnit.SECONDS.sleep(3);
        logger.info("Message persisted!");
        rp.processResponse(persistChatMsg.getMessage().getId());
    }
}
