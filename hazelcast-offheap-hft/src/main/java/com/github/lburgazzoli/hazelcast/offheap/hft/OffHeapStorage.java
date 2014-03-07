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

import com.google.common.collect.Sets;
import com.hazelcast.nio.serialization.ClassDefinition;
import com.hazelcast.nio.serialization.ClassDefinitionSetter;
import com.hazelcast.nio.serialization.Data;
import com.hazelcast.storage.DataRef;
import com.hazelcast.storage.Storage;
import net.openhft.collections.OffHeapUtil;
import net.openhft.collections.SharedHashMap;
import net.openhft.lang.model.DataValueClasses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Set;

/**
 * @author lburgazzoli
 */
public class OffHeapStorage implements Storage<DataRef> {
    private static final Logger LOGGER = LoggerFactory.getLogger(OffHeapStorage.class);

    private final SharedHashMap<Integer,OffHeapDataRef> m_dataRefMap;
    private final SharedHashMap<Integer,OffHeapDataVal> m_dataValMap;
    private final Set<ClassDefinition> m_defs;
    private final ThreadLocal<OffHeapDataVal> m_thData;

    /**
     * c-tor
     *
     * @param path
     */
    public OffHeapStorage(String path) throws IOException {
        m_defs       = Sets.newConcurrentHashSet();
        m_dataRefMap = OffHeapUtil.getSharedHashMap(path, "data-ref", OffHeapDataRef.class);
        m_dataValMap = OffHeapUtil.getSharedHashMap(path, "data-val", OffHeapDataVal.class);

        m_thData = new ThreadLocal<OffHeapDataVal>() {
            @Override
            public OffHeapDataVal initialValue() {
                return new OffHeapDataVal();
            }
        };
    }

    // *************************************************************************
    //
    // *************************************************************************

    @Override
    public DataRef put(int hash, Data data) {

        OffHeapDataRef dr = new OffHeapDataRef(data);

        if(data.getClassDefinition() != null) {
            m_defs.add(data.getClassDefinition());
        }

        m_dataRefMap.put(hash, dr);

        OffHeapDataVal ofd = m_dataValMap.acquireUsing(hash,m_thData.get());
        ofd.set(data.getBuffer());

        return dr;
    }

    @Override
    public Data get(int hash, DataRef ref) {
        if(ref instanceof OffHeapDataRef) {
            OffHeapDataRef ohr = (OffHeapDataRef)ref;
            OffHeapDataVal ohv = m_dataValMap.getUsing(hash, m_thData.get());

            if(ohv != null) {
                return ClassDefinitionSetter.setClassDefinition(
                    getClassDefinition(ohr),
                    new Data(
                        ohr.getType(),
                        ohv.get()));
            }
        }

        return null;
    }

    @Override
    public void remove(int hash, DataRef ref) {
        //m_dataValMap.remove(hash);
        //m_dataRefMap.remove(hash);
        m_dataRefMap.remove(hash);
    }

    @Override
    public void destroy() {
        try {
            m_dataValMap.close();
        } catch (IOException e) {
            LOGGER.warn("DataValMap - IOException", e);
        }

        try {
            m_dataRefMap.close();
        } catch (IOException e) {
            LOGGER.warn("DataRefMap - IOException", e);
        }
    }

    // *************************************************************************
    //
    // *************************************************************************

    /**
     *
     * @param data
     * @return
     */
    private synchronized ClassDefinition getClassDefinition(OffHeapDataRef data) {
        for(ClassDefinition cd : m_defs) {
            if( cd.getClassId()   == data.getClassId()   &&
                cd.getFactoryId() == data.getFactoryId() &&
                cd.getVersion()   == data.getVersion()   ) {
                return cd;
            }
        }

        return null;
    }
}
