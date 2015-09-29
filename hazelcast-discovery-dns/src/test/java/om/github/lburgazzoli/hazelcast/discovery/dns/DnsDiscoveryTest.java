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
package om.github.lburgazzoli.hazelcast.discovery.dns;

import com.github.lburgazzoli.hazelcast.discovery.dns.DnsDiscoveryStrategyFactory;
import com.hazelcast.config.Config;
import com.hazelcast.config.InterfacesConfig;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.spi.discovery.DiscoveredNode;
import com.hazelcast.spi.discovery.DiscoveryMode;
import com.hazelcast.spi.discovery.DiscoveryStrategy;
import com.hazelcast.spi.discovery.DiscoveryStrategyFactory;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;


@Ignore
public class DnsDiscoveryTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(DnsDiscoveryTest.class);

    @Test
    public void discoveryProviderTest() {
        Map<String, Comparable> properties = new HashMap<>();
        properties.put("serviceName", "hazelcast.skydns.lb");

        DiscoveryStrategyFactory factory = new DnsDiscoveryStrategyFactory();
        DiscoveryStrategy provider = factory.newDiscoveryStrategy(properties);

        provider.start(DiscoveryMode.Member);

        Iterable<DiscoveredNode> nodes = provider.discoverNodes();
        assertNotNull(nodes);

        for(DiscoveredNode node : nodes) {
            LOGGER.info("Node -> {}", node.getPublicAddress());
        }
    }

    @Test
    public void hazelcastInstanceTest() throws Exception {
        final Config config = loadConfig("test-hazelcast-discovery-dns.xml");
        Hazelcast.newHazelcastInstance(config);
    }

    // *************************************************************************
    //
    // *************************************************************************

    private Config loadConfig(String fileName) throws IOException {

        try(InputStream in = DnsDiscoveryTest.class.getClassLoader().getResourceAsStream(fileName)) {
            Config config = new XmlConfigBuilder(in).build();

            InterfacesConfig interfaces = config.getNetworkConfig().getInterfaces();
            interfaces.clear();
            interfaces.setEnabled(true);
            interfaces.addInterface("127.0.0.1");

            return config;
        }
    }
}
