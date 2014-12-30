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
package com.github.lburgazzoli.hazelcast.serialization.fst;

import com.github.lburgazzoli.hazelcast.serialization.HzSerializationConstants;
import com.github.lburgazzoli.hazelcast.serialization.HzSerializer;
import com.github.lburgazzoli.hazelcast.serialization.HzStreamSerializer;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.Serializer;
import com.hazelcast.nio.serialization.StreamSerializer;
import org.nustaq.serialization.FSTConfiguration;
import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class FstSerializer<T> extends HzStreamSerializer<T, FSTConfiguration> {
    private FstSerializer(final Class<T> type, int typeId) {
        super(
            type,
            typeId,
            () -> {
                FSTConfiguration cfg = FSTConfiguration.createDefaultConfiguration();
                cfg.registerClass(type);

                return cfg;
            }
        );
    }

    @Override
    protected void streamedWrite(OutputStream outputStream, T object) throws IOException {
        FSTObjectOutput out = get().getObjectOutput(outputStream);
        out.writeObject(object);
        out.flush();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected T streamedRead(InputStream inputStream) throws IOException {
        FSTObjectInput in = get().getObjectInput(inputStream);

        try {
            return (T)in.readObject();
        } catch(Exception e) {
            throw new IOException(e);
        }
    }

    // *************************************************************************
    //
    // *************************************************************************

    public static <V> Serializer make(final Class<V> type) {
        return new FstSerializer<>(type, HzSerializationConstants.TYPEID_FST);
    }
}
