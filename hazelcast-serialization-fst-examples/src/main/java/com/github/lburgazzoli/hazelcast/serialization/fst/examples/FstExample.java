/*
 * Copyright 2014 Luca Burgazzoli
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
package com.github.lburgazzoli.hazelcast.serialization.fst.examples;


import com.github.lburgazzoli.hazelcast.serialization.fst.FstSerializer;
import com.hazelcast.config.Config;
import com.hazelcast.config.InMemoryFormat;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.config.SerializerConfig;
import com.hazelcast.core.EntryAdapter;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class FstExample {
    private static final Logger LOGGER = LoggerFactory.getLogger(FstExample.class);

    // *************************************************************************
    //
    // *************************************************************************

    private static class SimpleEntryListener extends EntryAdapter<String, FstCustomer> {
        private final String m_id;

        public SimpleEntryListener(String id) {
            m_id = id;
        }

        public void entryAdded(EntryEvent<String, FstCustomer> event) {
            try {
                LOGGER.info("{} - {} => {}",
                    m_id,
                    event.getKey(),
                    event.getValue().toString());
            } catch (Exception e) {
                LOGGER.warn("Exception",e);
            }
        }
    }

    // *************************************************************************
    //
    // *************************************************************************

    private HazelcastInstance newHzInstance() {
        Config cfg = new Config();
        cfg.setProperty("hazelcast.logging.type","log4j2");

        cfg.getSerializationConfig().getSerializerConfigs().add(
            new SerializerConfig()
                .setTypeClass(FstCustomer.class)
                .setImplementation(FstSerializer.make(FstCustomer.class))
        );

        NetworkConfig network = cfg.getNetworkConfig();
        JoinConfig join = network.getJoin();
        join.getMulticastConfig().setEnabled(true);
        join.getTcpIpConfig().setEnabled(false);

        network.getInterfaces().setEnabled(false);

        MapConfig mapCfg = new MapConfig();
        mapCfg.setName("map.fst");
        mapCfg.setInMemoryFormat(InMemoryFormat.OBJECT);

        cfg.addMapConfig(mapCfg);

        return Hazelcast.newHazelcastInstance(cfg);
    }

    private void run() throws Exception {
        IMap<String,FstCustomer> m1 = newHzInstance().getMap("map.fst");
        m1.addEntryListener(
            new SimpleEntryListener("all"),
            true);

        Thread.sleep(1000 * 10);

        for(int i=0;i<10;i++) {
            m1.put("K_" + i, FstCustomer.newCustomer(i, "customer_" + i));
        }

        Thread.sleep(1000 * 10);

        Hazelcast.shutdownAll();
    }

    // *************************************************************************
    //
    // *************************************************************************

    public static void main(String[] args) {
        try {
            new FstExample().run();
        } catch (Exception e) {
            LOGGER.warn("Exception",e);
        }
    }
}
