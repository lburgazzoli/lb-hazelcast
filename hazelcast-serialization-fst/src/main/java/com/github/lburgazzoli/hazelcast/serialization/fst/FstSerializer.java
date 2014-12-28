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
package com.github.lburgazzoli.hazelcast.serialization.fst;

import com.github.lburgazzoli.hazelcast.serialization.HzSerializationConstants;
import com.github.lburgazzoli.hazelcast.serialization.HzSerializer;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.StreamSerializer;
import org.nustaq.serialization.FSTConfiguration;
import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class FstSerializer<T> extends HzSerializer<T> implements StreamSerializer<T> {
    private final ThreadLocal<FSTConfiguration> m_fstConf;

    private FstSerializer(final Class<T> type, int typeId) {
        super(type, typeId);

        m_fstConf = new ThreadLocal<FSTConfiguration>() {
            @Override
            protected FSTConfiguration initialValue() {
                FSTConfiguration cfg = FSTConfiguration.createDefaultConfiguration();
                cfg.registerClass(type);

                return cfg;
            }
        };
    }

    @Override
    public void write(ObjectDataOutput out, T object) throws IOException {
        FSTObjectOutput fstout = m_fstConf.get().getObjectOutput((OutputStream) out);
        fstout.writeObject(object);
        fstout.flush();
    }

    @Override
    public T read(ObjectDataInput in) throws IOException {
        FSTObjectInput fstin = m_fstConf.get().getObjectInput((InputStream)in);
        T result = null;

        try {
            result = (T)fstin.readObject();
        } catch(Exception e) {
            throw new IOException(e);
        }

        return result;
    }

    // *************************************************************************
    //
    // *************************************************************************

    public static <V> FstSerializer<V> make(final Class<V> type) {
        return new FstSerializer<V>(type, HzSerializationConstants.TYPEID_FST);
    }
}
