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
import com.hazelcast.nio.serialization.ByteArraySerializer;

import java.io.IOException;

public class JsonSerializer<T> implements ByteArraySerializer<T> {

    private final ObjectMapper m_mapper;
    private final Class<T> m_type;
    private final int m_typeId;

    public JsonSerializer(Class<T> type, int typeId) {
        m_type = type;
        m_typeId = typeId;
        m_mapper = new ObjectMapper();
    }

    public JsonSerializer(Class<T> type, int typeId, JsonFactory factory) {
        m_type = type;
        m_typeId = typeId;
        m_mapper = new ObjectMapper(factory);
    }

    @Override
    public int getTypeId() {
        return m_typeId;
    }

    @Override
    public void destroy() {
    }

    @Override
    public byte[] write(T object) throws IOException {
        return m_mapper.writeValueAsBytes(object);
    }

    @Override
    public T read(byte[] bytes) throws IOException {
        return m_mapper.readValue(bytes, m_type);
    }
}
