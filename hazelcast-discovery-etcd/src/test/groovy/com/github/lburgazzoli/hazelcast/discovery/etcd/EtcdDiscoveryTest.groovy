/*
 * Copyright 2015 Luca Burgazzoli
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
package com.github.lburgazzoli.hazelcast.discovery.etcd

import com.google.common.collect.Maps
import com.hazelcast.config.ClasspathXmlConfig
import com.hazelcast.config.Config
import com.hazelcast.core.Hazelcast
import com.hazelcast.core.HazelcastInstance
import com.hazelcast.spi.discovery.DiscoveredNode
import com.hazelcast.spi.discovery.DiscoveryMode
import com.hazelcast.spi.discovery.DiscoveryStrategy
import com.hazelcast.spi.discovery.DiscoveryStrategyFactory
import org.junit.After
import org.junit.Before
import org.junit.Test

class EtcdDiscoveryTest {

    // *************************************************************************
    //
    // *************************************************************************

    @Before
    public void setUp() {
        if (null == System.getenv("CI_TRAVIS")) {
            EtcdServerSimulator.setUp()
        }
    }

    @After
    public void tearDown() {
        if(null == System.getenv("CI_TRAVIS")) {
            EtcdServerSimulator.tearDown()
        }
    }

    // *************************************************************************
    //
    // *************************************************************************

    @Test
    public void discoveryProviderTest() {
        Map<String, Comparable> properties = Maps.newHashMap();
        properties.put(EtcdDiscovery.PROPERTY_URLS.key(), EtcdDiscovery.DEFAULT_ETCD_URL)
        properties.put(EtcdDiscovery.PROPERTY_SERVICE_NAME.key(),EtcdDiscovery.DEFAULT_SERVICE_NAME)

        DiscoveryStrategyFactory factory = new EtcdDiscoveryStrategyFactory()
        DiscoveryStrategy provider = factory.newDiscoveryStrategy(properties)

        provider.start(DiscoveryMode.Member)

        Collection<DiscoveredNode> nodes = provider.discoverNodes()

        assert nodes
        assert EtcdDiscoveryTestSupport.NODES.size() == nodes.size()

        nodes.each { node ->
            assert node.properties
            assert 1 == node.properties.size()
            assert node.properties['name']

            def name = node.properties['name']
            def refs = EtcdDiscoveryTestSupport.NODES[name]

            assert refs['host'] == node.publicAddress.host

            if(refs['port']) {
                assert refs['port'] == node.publicAddress.port
            } else {
                assert EtcdDiscovery.DEFAULT_HZ_PORT == node.publicAddress.port
            }
        }
    }

    @Test
    public void hazelcastInstanceTest() throws Exception {
        final Config config = loadConfig("test-hazelcast-discovery-etcd.xml")
        final HazelcastInstance hz = Hazelcast.newHazelcastInstance(config)

        assert hz
        assert 1 ==  hz.cluster.members.size()

        hz.shutdown()
    }

    // *************************************************************************
    //
    // *************************************************************************

    private Config loadConfig(String fileName) throws IOException {
        def cfg = new ClasspathXmlConfig(fileName)

        cfg.networkConfig.interfaces.clear()
        cfg.networkConfig.interfaces.setEnabled(true)
        cfg.networkConfig.interfaces.addInterface("127.0.0.1")

        return cfg
    }
}
