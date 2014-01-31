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

import com.hazelcast.instance.DefaultNodeInitializer;
import com.hazelcast.instance.Node;
import com.hazelcast.storage.DataRef;
import com.hazelcast.storage.Storage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lburgazzoli
 */
public class OffHeapNodeInitializer extends DefaultNodeInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(OffHeapNodeInitializer.class);

    private long m_size;

    /**
     * c-tor
     */
    public OffHeapNodeInitializer() {
        m_size = -1;
    }

    // *************************************************************************
    //
    // *************************************************************************

    @Override
    public void afterInitialize(Node node) {
        super.afterInitialize(node);

        String size = node.getConfig().getProperty("com.github.lburgazzoli.hazelcast.offheap.mapdb.size");

        if(StringUtils.isNotBlank(size)) {
            m_size = Long.parseLong(size);
            LOGGER.debug("OffHeap size: {}", m_size);
        }
    }

    @Override
    public Storage<DataRef> getOffHeapStorage() {
        return new OffHeapStorage(m_size);
    }
}
