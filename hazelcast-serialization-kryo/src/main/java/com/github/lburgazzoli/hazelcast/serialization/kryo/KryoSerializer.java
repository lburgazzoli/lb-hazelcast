/*
 * Copyright 2014 Luca Burgazzoli
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
package com.github.lburgazzoli.hazelcast.serialization.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.io.UnsafeInput;
import com.esotericsoftware.kryo.io.UnsafeOutput;
import com.github.lburgazzoli.hazelcast.serialization.HzSerializationConstants;
import com.github.lburgazzoli.hazelcast.serialization.HzSerializer;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.Serializer;
import com.hazelcast.nio.serialization.StreamSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class KryoSerializer<T> extends HzSerializer<T, Kryo> implements StreamSerializer<T> {

    private KryoSerializer(final Class<T> type, int typeId) {
        super(
            type,
            typeId,
            () -> {
                Kryo kryo = new Kryo();
                kryo.register(type);

                return kryo;
            }
        );
    }

    @Override
    public void write(ObjectDataOutput out, T object) throws IOException {
        try(Output output = new UnsafeOutput((OutputStream) out)) {
            get().writeObject(output, object);
            output.flush();
            output.close();
        }
    }

    @Override
    public T read(ObjectDataInput in) throws IOException {
        T object = null;

        try(final Input input = new UnsafeInput((InputStream) in)) {
            object = get().readObject(input, getType());
            input.close();
        }


        return object;
    }

    // *************************************************************************
    //
    // *************************************************************************

    public static <V> Serializer make(final Class<V> type) {
        return new KryoSerializer<>(type, HzSerializationConstants.TYPEID_KRYO);
    }
}
