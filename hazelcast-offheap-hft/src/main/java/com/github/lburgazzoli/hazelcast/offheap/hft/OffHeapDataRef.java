/*
 * Copyright 2014 lb
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
package com.github.lburgazzoli.hazelcast.offheap.hft;

import com.hazelcast.nio.serialization.ClassDefinition;
import com.hazelcast.nio.serialization.Data;
import com.hazelcast.storage.DataRef;
import net.openhft.lang.io.Bytes;
import net.openhft.lang.io.serialization.BytesMarshallable;
import org.jetbrains.annotations.NotNull;


/**
 * @author lburgazzoli
 */
public class OffHeapDataRef implements DataRef, BytesMarshallable {
    private int m_type;
    private int m_size;
    private int m_factoryId;
    private int m_classId;
    private int m_version;

    /**
     * c-tor
     */
    public OffHeapDataRef() {
        m_type      = -1;
        m_size      = -1;
        m_factoryId = -1;
        m_classId   = -1;
        m_version   = -1;
    }

    /**
     * c-tor
     *
     * @param data
     */
    public OffHeapDataRef(Data data) {
        m_type      = data.getType();
        m_size      = data.getBuffer().length;
        m_factoryId = data.getClassDefinition().getFactoryId();
        m_classId   = data.getClassDefinition().getClassId();
        m_version   = data.getClassDefinition().getVersion();
    }

    // *************************************************************************
    // DataRef
    // *************************************************************************

    @Override
    public int size() {
        return m_size;
    }

    @Override
    public int heapCost() {
        return 21;
    }

    // *************************************************************************
    // Getters
    // *************************************************************************

    /**
     *
     * @return
     */
    public int getType() {
        return m_type;
    }

    /**
     *
     * @return
     */
    public int getFactoryId() {
        return m_factoryId;
    }

    /**
     *
     * @return
     */
    public int getClassId() {
        return m_classId;
    }

    /**
     *
     * @return
     */
    public int getVersion() {
        return m_version;
    }

    // *************************************************************************
    // BytesMarshallable
    // *************************************************************************

    @Override
    public void readMarshallable(@NotNull Bytes in) throws IllegalStateException {
        m_type      = in.readInt();
        m_size      = in.readInt();
        m_factoryId = in.readInt();
        m_classId   = in.readInt();
        m_version   = in.readInt();
    }

    @Override
    public void writeMarshallable(@NotNull Bytes out) {
        out.writeInt(m_type);
        out.writeInt(m_size);
        out.writeInt(m_factoryId);
        out.writeInt(m_classId);
        out.writeInt(m_version);
    }
}
