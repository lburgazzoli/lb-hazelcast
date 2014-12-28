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

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.smile.SmileFactory;
import com.github.lburgazzoli.hazelcast.serialization.HzSerializationConstants;
import com.github.lburgazzoli.hazelcast.serialization.HzSerializer;
import com.hazelcast.nio.serialization.ByteArraySerializer;

import java.io.IOException;

public final class JsonSerializer<T> extends HzSerializer<T> implements ByteArraySerializer<T> {
    private final ObjectMapper m_mapper;

    private JsonSerializer(Class<T> type, int typeId) {
        this(type, typeId, null);
    }

    private JsonSerializer(Class<T> type, int typeId, final JsonFactory factory) {
        super(type, typeId);
        m_mapper = new ObjectMapper(factory);
    }

    @Override
    public byte[] write(T object) throws IOException {
        return m_mapper.writeValueAsBytes(object);
    }

    @Override
    public T read(byte[] bytes) throws IOException {
        return m_mapper.readValue(bytes, getType());
    }

    // *************************************************************************
    //
    // *************************************************************************

    public static <V> JsonSerializer<V> makeBinary(final Class<V> type) {
        return new JsonSerializer<V>(
            type,
            HzSerializationConstants.TYPEID_JSON_BINARY,
            new SmileFactory()
        );
    }

    public static <V> JsonSerializer<V> makePlain(final Class<V> type) {
        return new JsonSerializer<V>(
            type,
            HzSerializationConstants.TYPEID_JSON_PLAIN
        );
    }
}
