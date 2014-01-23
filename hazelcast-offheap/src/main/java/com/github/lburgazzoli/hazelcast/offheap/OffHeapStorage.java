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

import com.google.common.collect.Maps;
import com.hazelcast.nio.serialization.Data;
import com.hazelcast.storage.DataRef;
import com.hazelcast.storage.Storage;
import net.openhft.lang.io.DirectStore;

import java.util.Map;

/**
 * @author lburgazzoli
 */
public class OffHeapStorage implements Storage<DataRef> {
    private final DirectStore m_store;
    private final Map<Integer,OffHeapDataRef> m_dataRefs;

    /**
     * c-tor
     *
     * @param size
     * @param lazy
     */
    public OffHeapStorage(long size, boolean lazy) {
        m_dataRefs = Maps.newHashMap();
        m_store = new DirectStore(null,size,lazy);
    }

    // *************************************************************************
    //
    // *************************************************************************

    @Override
    public DataRef put(int hash, Data data) {
        OffHeapDataRef dataref = m_dataRefs.get(hash);

        if(dataref != null) {
            dataref.destroy();

            //TODO: reuse chunk OffHeapDataRef(DirectStore,long)
            dataref = new OffHeapDataRef(m_store,data);
        } else {
            dataref = new OffHeapDataRef(m_store,data);
        }

        m_dataRefs.put(hash,dataref);

        return dataref;
    }

    @Override
    public Data get(int hash, DataRef ref) {
        return ((OffHeapDataRef)ref).asData();
    }

    @Override
    public void remove(int hash, DataRef ref) {
        ((OffHeapDataRef)ref).destroy();
        m_dataRefs.remove(hash);
    }

    @Override
    public void destroy() {
        m_store.free();
    }
}
