package org.ingini.jactor.chat;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.JAFuture;
import org.fest.reflect.core.Reflection;
import org.ingini.jactor.chat.actor.ChatActor;
import org.ingini.jactor.chat.conf.JActorConfig;
import org.ingini.jactor.chat.domain.*;
import org.ingini.jactor.chat.service.JActorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

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
@ContextConfiguration(classes = {JActorConfig.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class TestChat {

    @Autowired
    private JActorService jActorService;

    @Test
    public void testProcess() throws Exception {
        //GIVEN
        UserId jactor101Id = new UserId(101);
        ChatActor jactor101 = jActorService.findOrInit(jactor101Id);
        UserId jactor201Id = new UserId(201);
        ChatActor jactor201 = jActorService.findOrInit(jactor201Id);

        Logger logger101Mock = mock(Logger.class);
        Reflection.field("logger").ofType(Logger.class).in(jactor101).set(logger101Mock);
        Logger logger201Mock = mock(Logger.class);
        Reflection.field("logger").ofType(Logger.class).in(jactor201).set(logger201Mock);

        //WHEN
        ChatMsg chatMsg201 = new ChatMsg(new MessageId(1), jactor201Id, "Hi, I'm 201!");
        chatMsg201.sendEvent(jactor101);

        ChatMsg chatMsg101 = new ChatMsg(new MessageId(2), jactor101Id, "Hi, I'm 101!");
        chatMsg101.sendEvent(jactor201);

        //THEN
        verify(logger101Mock, never()).info(eq("Actor {} received new msg ({}) from 201"), eq(jactor101Id), eq(chatMsg201.getContent()));
        verify(logger201Mock, never()).info(eq("Actor {} received new msg ({}) from 101"), eq(jactor201Id), eq(chatMsg101.getContent()));
        verify(logger101Mock, timeout(5000).times(1)).info(eq("Actor {} received new msg ({}) from 201"), eq(jactor101Id), eq(chatMsg201.getContent()));
        verify(logger201Mock, timeout(5000).times(1)).info(eq("Actor {} received new msg ({}) from 101"), eq(jactor201Id), eq(chatMsg101.getContent()));
    }

    @Test
    public void testGet() throws Exception {
        //GIVEN
        MsgConfirmation confirmationChatMsg = new MsgConfirmation(new MessageId(1));
        UserId jactorId = new UserId(401);
        Actor jactor = jActorService.findOrInit(jactorId);

        Logger logger101Mock = mock(Logger.class);
        Reflection.field("logger").ofType(Logger.class).in(jactor).set(logger101Mock);

        //WHEN
        JAFuture future = new JAFuture();
        Object result = future.send(jactor, confirmationChatMsg);

        //THEN
        assertThat(result).isEqualTo(Confirmation.NO);

        verify(logger101Mock, times(1)).info(eq("Start sleeping"));
        verify(logger101Mock, times(1)).info(eq("Stop sleeping"));

    }

    @Test
    public void testPersist() throws Exception {
        //GIVEN
        UserId jactorId = new UserId(601);
        ChatActor jactor = jActorService.findOrInit(jactorId);

        Logger loggerMock = mock(Logger.class);
        Reflection.field("logger").ofType(Logger.class).in(jactor).set(loggerMock);

        MessageId messageId = new MessageId(1);
        UserId sender = new UserId(701);
        ChatMsg chatMsg = new ChatMsg(messageId, sender, "Test msg");

        //WHEN
        jactor.process(chatMsg);

        //THEN
        verify(loggerMock, timeout(5000).times(1)).info(eq("Actor {} received new msg ({}) from " + sender.getCode()), eq(jactorId), eq(chatMsg.getContent()));

        //AND GIVEN
        MsgConfirmation confirmationChatMsg = new MsgConfirmation(messageId);

        //WHEN
        JAFuture future = new JAFuture();
        Confirmation confirmation = confirmationChatMsg.send(future, jactor);

        //THEN
        assertThat(confirmation).isEqualTo(Confirmation.YES);

        //AND WHEN
        jactor.persist(messageId);

        confirmation = confirmationChatMsg.send(future, jactor);

        //THEN
        assertThat(confirmation).isEqualTo(Confirmation.NO);

    }
}
