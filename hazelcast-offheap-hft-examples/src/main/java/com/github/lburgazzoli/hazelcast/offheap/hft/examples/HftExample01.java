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
package com.github.lburgazzoli.hazelcast.offheap.hft.examples;


import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 *
 */
public final class HftExample01 {
    private static final Logger LOGGER = LoggerFactory.getLogger(HftExample01.class);
    private static final String MAPNAME = "map.offheap";

    // *************************************************************************
    //
    // *************************************************************************

    /**
     *
     * @return
     */
    private HazelcastInstance newHzInstance() {
        String basePath = System.getProperty("java.io.tmpdir") + "/hz-offheap";
        new File(basePath).mkdirs();

        Config cfg = new Config();
        cfg.setProperty("hazelcast.logging.type", "slf4j");
        cfg.setProperty("com.github.lburgazzoli.hazelcast.offheap.hft.path",basePath);

        NetworkConfig network = cfg.getNetworkConfig();
        JoinConfig join = network.getJoin();
        join.getMulticastConfig().setEnabled(true);
        join.getTcpIpConfig().setEnabled(false);

        network.getInterfaces().setEnabled(false);

        MapConfig mapCfg = new MapConfig();
        mapCfg.setName(MAPNAME);
        mapCfg.setInMemoryFormat(InMemoryFormat.OFFHEAP);
        cfg.addMapConfig(mapCfg);

        return Hazelcast.newHazelcastInstance(cfg);
    }

    /**
     *
     * @throws Exception
     */
    private void run() throws Exception {
        IMap<String,String> map = newHzInstance().getMap(MAPNAME);
        LOGGER.debug("put {}",map.put("key1","val1"));
        LOGGER.debug("put {}",map.put("key2","val2"));
        LOGGER.debug("get {}",map.get("key1"));
        LOGGER.debug("get {}",map.get("key2"));

        Hazelcast.shutdownAll();
    }

    // *************************************************************************
    //
    // *************************************************************************

    public static void main(String[] args) {
        try {
            new HftExample01().run();
        } catch (Exception e) {
            LOGGER.warn("Exception",e);
        }
    }
}
