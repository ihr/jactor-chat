package org.ingini.jactor.chat.domain;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.agilewiki.jactor.lpc.Request;
import org.ingini.jactor.chat.actor.StorageActor;

import javax.annotation.concurrent.Immutable;

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
@Immutable
public class PersistChatMsg extends Request<MessageId, StorageActor> {

    private final ChatMsg message;
    public PersistChatMsg(ChatMsg message) {
        this.message = message;
    }

    @Override
    public boolean isTargetType(Actor targetActor) {
        return targetActor instanceof StorageActor;
    }

    @Override
    public void processRequest(JLPCActor targetActor, RP rp) throws Exception {
       ((StorageActor) targetActor).process(this, rp);
    }

    public ChatMsg getMessage() {
        return message;
    }
}
