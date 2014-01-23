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
package com.github.lburgazzoli.hazelcast.offheap;

import com.hazelcast.nio.serialization.ClassDefinition;
import com.hazelcast.nio.serialization.ClassDefinitionSetter;
import com.hazelcast.nio.serialization.Data;
import com.hazelcast.storage.DataRef;
import net.openhft.lang.io.DirectBytes;
import net.openhft.lang.io.DirectStore;

/**
 * @author lburgazzoli
 */
public class OffHeapDataRef implements DataRef {
    private final int m_type;
    private final ClassDefinition m_classDefinition;
    private final DirectBytes m_data;

    /**
     *
     * @param store
     * @param data
     */
    public OffHeapDataRef(DirectStore store,Data data) {
        m_type = data.getType();
        m_classDefinition = data.getClassDefinition();


        m_data = store.createSlice();
        m_data.write(data.getBuffer());
    }

    /**
     *
     * @param store
     * @param address
     */
    public OffHeapDataRef(DirectStore store,long address) {
        m_type = -1;
        m_classDefinition = null;
        m_data = null;

        /*
        m_type = data.getType();
        m_classDefinition = data.getClassDefinition();
        m_data = store.createSlice();
        m_data.write(data.getBuffer());
        */
    }

    // *************************************************************************
    // DataRef
    // *************************************************************************

    @Override
    public int size() {
        return (int)m_data.capacity();
    }

    @Override
    public int heapCost() {
        return 21;
    }

    // *************************************************************************
    //
    // *************************************************************************

    /**
     *
     * @return
     */
    public long address() {
        return m_data.startAddr();
    }

    /**
     *
     * @return
     */
    public Data asData() {
        byte[] buffer = new byte[size()];
        m_data.read(buffer);

        return ClassDefinitionSetter.setClassDefinition(m_classDefinition,new Data(m_type,buffer));
    }

    /**
     *
     */
    public void destroy() {
        m_data.close();
    }
}
