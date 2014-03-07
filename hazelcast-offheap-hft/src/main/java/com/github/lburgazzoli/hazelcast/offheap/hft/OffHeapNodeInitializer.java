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

import com.hazelcast.instance.DefaultNodeInitializer;
import com.hazelcast.instance.Node;
import com.hazelcast.storage.DataRef;
import com.hazelcast.storage.Storage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * @author lburgazzoli
 */
public class OffHeapNodeInitializer extends DefaultNodeInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(OffHeapNodeInitializer.class);

    private String m_path;

    /**
     * c-tor
     */
    public OffHeapNodeInitializer() {
        m_path = null;
    }

    // *************************************************************************
    //
    // *************************************************************************

    @Override
    public void afterInitialize(Node node) {
        super.afterInitialize(node);

        Properties props =  node.getConfig().getProperties();
        String     path  = props.getProperty("com.github.lburgazzoli.hazelcast.offheap.hft.path");

        if(StringUtils.isNotBlank(path)) {
            m_path = path;
            LOGGER.debug("OffHeap path: {}", m_path);
        }
    }

    @Override
    public Storage<DataRef> getOffHeapStorage() {
        try {
            return new OffHeapStorage(m_path);
        } catch(IOException e) {
            LOGGER.warn("IOException",e);
        }

        return super.getOffHeapStorage();
    }
}
