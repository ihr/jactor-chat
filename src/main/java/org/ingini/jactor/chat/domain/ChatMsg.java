package org.ingini.jactor.chat.domain;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.agilewiki.jactor.lpc.Request;
import org.ingini.jactor.chat.actor.ChatActor;

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
public class ChatMsg extends Request<Void, ChatActor> {

    private final MessageId id;
    private final UserId sender;
    private final String content;

    public ChatMsg(MessageId id, UserId sender, String content) {
        this.id = id;
        this.sender = sender;
        this.content = content;
    }

    @Override
    public boolean isTargetType(Actor targetActor) {
        return targetActor instanceof ChatActor;
    }

    @Override
    public void processRequest(JLPCActor targetActor, RP rp) throws Exception {
       ((ChatActor) targetActor).process(this);

        //Finished processing msg
        rp.processResponse(null);
    }

    public MessageId getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public UserId getSender() {
        return sender;
    }

    @Override
    public String toString() {
        return "ChatMsg{" +
                "sender=" + sender +
                ", content='" + content + '\'' +
                '}';
    }
}
