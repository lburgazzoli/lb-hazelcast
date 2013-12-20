/**
 *
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
package com.github.lburgazzoli.hazelcast.json.examples;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.lburgazzoli.hazelcast.json.serializer.PlainJsonSerializer;
import com.hazelcast.config.*;
import com.hazelcast.core.*;
import com.hazelcast.query.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;
import java.util.Random;

/**
 *
 */
public class Example01 {
    private static final Logger LOGGER = LoggerFactory.getLogger(Example01.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    // *************************************************************************
    //
    // *************************************************************************

    /**
     *
     */
    private static class SimpleThresholdPredicate implements Predicate<String,JsonNode> {
        private final int m_threshold;

        /**
         * c-tor
         *
         * @param threshold
         */
        public SimpleThresholdPredicate(int threshold) {
            m_threshold = threshold;
        }

        @Override
        public boolean apply(Map.Entry<String, JsonNode> mapEntry) {
            JsonNode node = mapEntry.getValue();
            return node.get("data").get("value_1").asInt() >= m_threshold;
        }
    };

    /**
     *
     */
    private static class SimpleEntryListener extends EntryAdapter<String, JsonNode> {
        private final String m_id;

        /**
         * c-tor
         *
         * @param id
         */
        public SimpleEntryListener(String id) {
            m_id = id;
        }

        public void entryAdded(EntryEvent<String, JsonNode> event) {
            try {
                LOGGER.debug("{} - {} => {}",
                    m_id,
                    event.getKey(),
                    MAPPER.writeValueAsString(event.getValue()));
            } catch (Exception e) {
                LOGGER.warn("Exception",e);
            }
        }
    }

    // *************************************************************************
    //
    // *************************************************************************

    /**
     *
     * @return
     */
    private HazelcastInstance newHzInstance() {
        Config cfg = new Config();
        cfg.setProperty("hazelcast.logging.type","slf4j");

        cfg.getSerializationConfig().getSerializerConfigs().add(
            new SerializerConfig().
                setTypeClass(ObjectNode.class).
                setImplementation(new PlainJsonSerializer(JsonNode.class))
        );

        NetworkConfig network = cfg.getNetworkConfig();
        JoinConfig join = network.getJoin();
        join.getMulticastConfig().setEnabled(true);
        join.getTcpIpConfig().setEnabled(false);

        network.getInterfaces().setEnabled(false);

        MapConfig mapCfg = new MapConfig();
        mapCfg.setName("map.json");
        mapCfg.setInMemoryFormat(InMemoryFormat.OBJECT);

        cfg.addMapConfig(mapCfg);

        return Hazelcast.newHazelcastInstance(cfg);
    }

    private void run() throws Exception {
        IMap<String,JsonNode> m1 = newHzInstance().getMap("map.json");
        IMap<String,JsonNode> m2 = newHzInstance().getMap("map.json");;

        m2.addEntryListener(
            new SimpleEntryListener("all"),
            true);
        m2.addEntryListener(
            new SimpleEntryListener("Threshold_50"),
            new SimpleThresholdPredicate(50),
            true);
        m2.addEntryListener(
            new SimpleEntryListener("Threshold_80"),
            new SimpleThresholdPredicate(80),
            true);

        Thread.sleep(1000 * 10);

        for(int i=0;i<10;i++) {
            ObjectNode on = MAPPER.createObjectNode();
            on.with("node")
                .put("id", i)
                .put("timestamp", new Date().toString());
            on.with("data")
                .put("value_1", new Random().nextInt(100))
                .put("value_2", new Random().nextInt(100));

            m1.put("K_" + i,on);
        }

        Thread.sleep(1000 * 10);

        Hazelcast.shutdownAll();
    }

    // *************************************************************************
    //
    // *************************************************************************

    public static void main(String[] args) {
        try {
            new Example01().run();
        } catch (Exception e) {
            LOGGER.warn("Exception",e);
        }
    }
}
