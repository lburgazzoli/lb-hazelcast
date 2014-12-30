/*
 * Copyright 2013 lb
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.lburgazzoli.hazelcast.serialization.json;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonEnvelope {
    private final byte[] data;

    public JsonEnvelope(final byte[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return new String(this.data);
    }

    byte[] data() {
        return this.data;
    }

    // *************************************************************************
    //
    // *************************************************************************

    public static <T> T asObject(
        JsonEnvelope envelope, ObjectMapper mapper, Class<T> type) throws IOException {
        return mapper.readValue(envelope.data, type);
    }

    public static <T> JsonEnvelope fromObject(ObjectMapper mapper, T object) throws IOException {
        return new JsonEnvelope(mapper.writeValueAsBytes(object));
    }
}
