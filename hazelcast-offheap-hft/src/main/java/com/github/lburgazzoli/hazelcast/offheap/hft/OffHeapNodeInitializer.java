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
import net.openhft.collections.SharedHashMap;
import net.openhft.collections.SharedHashMapBuilder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.UUID;

/**
 * @author lburgazzoli
 */
public class OffHeapNodeInitializer extends DefaultNodeInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(OffHeapNodeInitializer.class);

    private Properties m_props;

    /**
     * c-tor
     */
    public OffHeapNodeInitializer() {
        m_props = null;
    }

    // *************************************************************************
    //
    // *************************************************************************

    @Override
    public void afterInitialize(Node node) {
        super.afterInitialize(node);
        m_props = node.getConfig().getProperties();
    }

    @Override
    public Storage<DataRef> getOffHeapStorage() {
        try {
            return new OffHeapStorage(getSharedHashMap(m_props));
        } catch(IOException e) {
            LOGGER.warn("IOException",e);
        }

        return super.getOffHeapStorage();
    }

    /**
     *
     * @return
     * @throws IOException
     */
    public static SharedHashMap<Integer,OffHeapDataVal> getSharedHashMap(Properties props) throws IOException {

        SharedHashMapBuilder shmb = new SharedHashMapBuilder();

        String entries = props.getProperty("com.github.lburgazzoli.hazelcast.offheap.hft.entries");
        if(StringUtils.isNotBlank(entries)) {
            shmb.entries(Long.parseLong(entries));
        }

        String entrySize = props.getProperty("com.github.lburgazzoli.hazelcast.offheap.hft.entrySize");
        if(StringUtils.isNotBlank(entrySize)) {
            shmb.entrySize(Integer.parseInt(entrySize));
        }

        String minSegments = props.getProperty("com.github.lburgazzoli.hazelcast.offheap.hft.minSegments");
        if(StringUtils.isNotBlank(minSegments)) {
            shmb.minSegments(Integer.parseInt(minSegments));
        }

        String path  = props.getProperty("com.github.lburgazzoli.hazelcast.offheap.hft.path");
        String name  = props.getProperty("com.github.lburgazzoli.hazelcast.offheap.hft.name");
        String clean = props.getProperty("com.github.lburgazzoli.hazelcast.offheap.hft.cleanOnStart");

        if(StringUtils.isBlank(path)) {
            path = new File(FileUtils.getTempDirectoryPath(), UUID.randomUUID().toString()).getAbsolutePath();
        }
        if(StringUtils.isBlank(name)) {
            name = UUID.randomUUID().toString();
        }

        if(StringUtils.equalsIgnoreCase("true",clean)) {
            File fullPath = new File(path,name);
            LOGGER.debug("Deleting {} => {}",fullPath.getAbsolutePath(),fullPath.delete());
        }

        return shmb.create(
            new File(path,name),
            Integer.class,
            OffHeapDataVal.class);
    }
}
