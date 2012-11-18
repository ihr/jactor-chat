package org.ingini.jactor.chat.domain;

import com.google.common.base.Objects;

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
public class MessageId {

    private final long code;

    public MessageId(long code) {
        this.code = code;
    }

    public long getCode() {
        return code;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(code);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof MessageId)) {
            return false;
        }
        return Objects.equal(this.code, ((MessageId) obj).code);
    }

    @Override
    public String toString() {
        return String.valueOf(code);
    }
}
