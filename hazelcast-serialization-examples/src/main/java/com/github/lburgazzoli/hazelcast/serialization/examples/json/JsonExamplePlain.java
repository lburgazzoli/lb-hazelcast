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
package com.github.lburgazzoli.hazelcast.serialization.examples.json;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.lburgazzoli.hazelcast.serialization.json.JsonEnvelopeSerializer;
import com.github.lburgazzoli.hazelcast.serialization.json.JsonSerializer;
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
import com.hazelcast.nio.serialization.Serializer;
import com.hazelcast.query.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;
import java.util.Random;

public final class JsonExamplePlain {
    private static final Logger       LOGGER   = LoggerFactory.getLogger(JsonExamplePlain.class);
    private static final String       MAP_NAME = "map.json";
    private static final ObjectMapper MAPPER   = new ObjectMapper();

    // *************************************************************************
    //
    // *************************************************************************

    private static class ObjEntryListener extends EntryAdapter<String, JsonNode> {

        public void entryAdded(EntryEvent<String, JsonNode> event) {
            try {
                LOGGER.info("Obj - {} => {}",
                    event.getKey(),
                    MAPPER.writeValueAsString(event.getValue()));
            } catch (Exception e) {
                LOGGER.warn("Exception",e);
            }
        }
    }

    private static class TxtEntryListener extends EntryAdapter<String, String> {
        public void entryAdded(EntryEvent<String, String> event) {
            try {
                LOGGER.info("Txt - {} => {}",
                    event.getKey(),
                    event.getValue());
            } catch (Exception e) {
                LOGGER.warn("Exception",e);
            }
        }
    }

    // *************************************************************************
    //
    // *************************************************************************

    private HazelcastInstance newHzInstance(Class<?> typeClass, Serializer serializer) {
        Config cfg = new Config();
        cfg.setProperty("hazelcast.logging.type","log4j2");

        cfg.getSerializationConfig().getSerializerConfigs().add(
            new SerializerConfig()
                .setTypeClass(typeClass)
                .setImplementation(serializer)
        );

        NetworkConfig network = cfg.getNetworkConfig();
        JoinConfig join = network.getJoin();
        join.getMulticastConfig().setEnabled(true);
        join.getTcpIpConfig().setEnabled(false);

        network.getInterfaces().setEnabled(false);

        MapConfig mapCfg = new MapConfig();
        mapCfg.setName(MAP_NAME);
        mapCfg.setInMemoryFormat(InMemoryFormat.OBJECT);

        cfg.addMapConfig(mapCfg);

        return Hazelcast.newHazelcastInstance(cfg);
    }

    private void run() throws Exception {
        IMap<String,JsonNode> m1 = newHzInstance(
            JsonNode.class,
            JsonSerializer.makePlain(JsonNode.class)).getMap(MAP_NAME);

        IMap<String,JsonNode> m2 = newHzInstance(
            JsonNode.class,
            JsonSerializer.makePlain(JsonNode.class)).getMap(MAP_NAME);

        IMap<String,String>   m3 = newHzInstance(
            JsonEnvelopeSerializer.TYPE,
            JsonEnvelopeSerializer.INSTANCE).getMap(MAP_NAME);

        m2.addEntryListener(new ObjEntryListener(), true);
        m3.addEntryListener(new TxtEntryListener(), true);

        Thread.sleep(1000 * 2);

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
            new JsonExamplePlain().run();
        } catch (Exception e) {
            LOGGER.warn("Exception",e);
        }
    }
}
