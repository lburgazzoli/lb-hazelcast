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

import com.github.lburgazzoli.hazelcast.serialization.HzSerializationConstants;
import com.github.lburgazzoli.hazelcast.serialization.HzStreamSerializer;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.Serializer;

import java.io.IOException;

public final class JsonEnvelopeSerializer extends HzStreamSerializer<JsonEnvelope, JsonEnvelope> {
    public static final Class<JsonEnvelope>  TYPE     = JsonEnvelope.class;
    public static final Serializer           INSTANCE = make();

    private JsonEnvelopeSerializer(int typeId) {
        super(TYPE, typeId, null);
    }

    @Override
    public void write(ObjectDataOutput out, JsonEnvelope object) throws IOException {
        out.writeByteArray(object.data());
    }

    @Override
    public JsonEnvelope read(ObjectDataInput in) throws IOException {
        return new JsonEnvelope(in.readByteArray());
    }

    // *************************************************************************
    //
    // *************************************************************************

    public static Serializer make() {
        return new JsonEnvelopeSerializer(
            HzSerializationConstants.TYPEID_JSON_PLAIN
        );
    }
}
