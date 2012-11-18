package org.ingini.jactor.chat.domain;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.agilewiki.jactor.lpc.Request;
import org.ingini.jactor.chat.actor.ChatActor;

import javax.annotation.concurrent.Immutable;

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
@Immutable
public class MsgConfirmation extends Request<Confirmation, ChatActor> {

    private final MessageId messageId;

    public MsgConfirmation(MessageId messageId) {
        this.messageId = messageId;
    }

    public MessageId getMessageId() {
        return messageId;
    }

    @Override
    public boolean isTargetType(Actor targetActor) {
        return targetActor instanceof ChatActor;
    }

    @Override
    public void processRequest(JLPCActor targetActor, RP rp) throws Exception {
        ChatActor chatActor = (ChatActor) targetActor;
        rp.processResponse(chatActor.get(this));
    }
}
