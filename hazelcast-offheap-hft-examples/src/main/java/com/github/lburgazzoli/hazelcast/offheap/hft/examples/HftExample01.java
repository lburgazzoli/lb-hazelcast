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
package com.github.lburgazzoli.hazelcast.offheap.hft.examples;


import com.hazelcast.config.Config;
import com.hazelcast.config.InMemoryFormat;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import net.openhft.lang.Maths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 *
 */
public final class HftExample01 {
    private static final Logger LOGGER = LoggerFactory.getLogger(HftExample01.class);
    private static final String MAPNAME = "map-offheap";
    private static final int    COUNT   = 10000;

    // *************************************************************************
    //
    // *************************************************************************

    /**
     *
     * @return
     */
    private HazelcastInstance newHzInstance() {
        long entries  = Maths.nextPower2(10000L, 1024L);
        int  size     = 128;
        int  segments = Maths.nextPower2(COUNT, 1024);

        Config cfg = new Config();
        cfg.setProperty("hazelcast.logging.type", "slf4j");
        cfg.setProperty("com.github.lburgazzoli.hazelcast.offheap.hft.path",System.getProperty("java.io.tmpdir"));
        cfg.setProperty("com.github.lburgazzoli.hazelcast.offheap.hft.name",MAPNAME);
        cfg.setProperty("com.github.lburgazzoli.hazelcast.offheap.hft.entries",Long.toString(entries));
        cfg.setProperty("com.github.lburgazzoli.hazelcast.offheap.hft.entrySize",Integer.toString(size));
        cfg.setProperty("com.github.lburgazzoli.hazelcast.offheap.hft.minSegments",Integer.toString(segments));
        cfg.setProperty("com.github.lburgazzoli.hazelcast.offheap.hft.cleanOnStart","true");

        NetworkConfig network = cfg.getNetworkConfig();
        JoinConfig join = network.getJoin();
        join.getMulticastConfig().setEnabled(false);
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
        IMap<Integer,String> map = newHzInstance().getMap(MAPNAME);

        for(int i=0; i<COUNT; i++) {
            map.put(i,"val_" + i);
        }

        for(int i=0; i<COUNT; i++) {
            LOGGER.debug("get {}", map.get(i));
        }

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
