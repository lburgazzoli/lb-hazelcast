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
import com.github.lburgazzoli.hazelcast.serialization.HzStreamSerializer;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.Serializer;

import java.io.IOException;

public final class JsonSerializer<T> extends HzStreamSerializer<T, ObjectMapper> {
    private JsonSerializer(Class<T> type, int typeId) {
        this(type, typeId, null);
    }

    private JsonSerializer(Class<T> type, int typeId, final JsonFactory factory) {
        super(
            type,
            typeId,
            () -> new ObjectMapper(factory)
        );
    }

    @Override
    public void write(ObjectDataOutput out, T object) throws IOException {
        out.writeByteArray(get().writeValueAsBytes(object));
    }

    @Override
    public T read(ObjectDataInput in) throws IOException {
        return get().readValue(in.readByteArray(), getType());
    }

    // *************************************************************************
    //
    // *************************************************************************

    public static <V> Serializer makeBinary(final Class<V> type) {
        return new JsonSerializer<>(
            type,
            HzSerializationConstants.TYPEID_JSON_BINARY,
            new SmileFactory()
        );
    }

    public static <V> Serializer makePlain(final Class<V> type) {
        return new JsonSerializer<>(
            type,
            HzSerializationConstants.TYPEID_JSON_PLAIN
        );
    }
}
