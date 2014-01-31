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
package com.github.lburgazzoli.hazelcast.offheap.mapdb;

import com.hazelcast.nio.serialization.Data;
import com.hazelcast.storage.DataRef;
import com.hazelcast.storage.Storage;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lburgazzoli
 */
public class OffHeapStorage implements Storage<DataRef> {
    private static final Logger LOGGER = LoggerFactory.getLogger(OffHeapStorage.class);

    private final DB m_store;
    private final HTreeMap m_map;

    /**
     * c-tor
     */
    public OffHeapStorage(long size) {
        m_store = size > 1
            ? DBMaker.newDirectMemoryDB().cacheDisable().sizeLimit(size).make()
            : DBMaker.newDirectMemoryDB().cacheDisable().make();

        m_map = m_store.createHashMap("hz_offheap").make();
    }

    // *************************************************************************
    //
    // *************************************************************************

    @Override
    public DataRef put(int hash, Data data) {
        LOGGER.debug("put {} -> <{}>",hash,data.getBuffer());

        DataRef dref = new OffHeapDataRef(data);
        m_map.put(hash,dref);

        return dref;
    }

    @Override
    public Data get(int hash, DataRef ref) {
        LOGGER.debug("get {} -> {}",hash,ref.size());
        return ((OffHeapDataRef)ref).getData();
    }

    @Override
    public void remove(int hash, DataRef ref) {
        LOGGER.debug("remove {} -> {}", hash, ref.size());
        m_map.remove(hash);
    }

    @Override
    public void destroy() {
        LOGGER.debug("destroy");
        m_store.close();
    }
}
