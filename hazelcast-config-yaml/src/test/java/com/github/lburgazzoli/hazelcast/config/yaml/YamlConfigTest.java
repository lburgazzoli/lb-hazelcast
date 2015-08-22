/*
 * Copyright (c) 2015 Luca Burgazzoli
 *
 * https://github.com/lburgazzoli
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.lburgazzoli.hazelcast.config.yaml;

import com.hazelcast.config.Config;
import com.hazelcast.config.InterfacesConfig;
import com.hazelcast.config.MulticastConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.config.TcpIpConfig;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class YamlConfigTest {
    public static final String RESOURCE = "/hazelcast/hazelcast.yml";

    @Test
    public void testBuilder() {
        final Config config = new YamlConfigBuilder(getResource()).build();
        assertNotNull(config);

        // Group Config
        assertNotNull(config.getGroupConfig());
        assertEquals("group-name", config.getGroupConfig().getName());
        assertEquals("group-password", config.getGroupConfig().getPassword());

        // Network Config
        final NetworkConfig netCfg = config.getNetworkConfig();

        assertEquals(true, netCfg.isReuseAddress());
        assertEquals(5900, netCfg.getPort());
        assertEquals(false, netCfg.isPortAutoIncrement());
        assertEquals(100, netCfg.getPortCount());
        assertFalse(netCfg.getOutboundPortDefinitions().isEmpty());
        assertEquals(2, netCfg.getOutboundPortDefinitions().size());
        assertTrue(netCfg.getOutboundPortDefinitions().contains("10100"));
        assertTrue(netCfg.getOutboundPortDefinitions().contains("9000-10000"));
        assertEquals("127.0.0.1", netCfg.getPublicAddress());

        // Multicast Config
        final MulticastConfig mcastCfg = netCfg.getJoin().getMulticastConfig();
        assertEquals(false, mcastCfg.isEnabled());
        assertEquals(false, mcastCfg.isLoopbackModeEnabled());
        assertFalse(mcastCfg.getTrustedInterfaces().isEmpty());
        assertEquals(2, mcastCfg.getTrustedInterfaces().size());
        assertTrue(mcastCfg.getTrustedInterfaces().contains("eth0"));
        assertTrue(mcastCfg.getTrustedInterfaces().contains("eth1"));

        // TcpIp Config
        final TcpIpConfig tcpCfg = netCfg.getJoin().getTcpIpConfig();
        assertEquals(false, tcpCfg.isEnabled());
        assertEquals(10, tcpCfg.getConnectionTimeoutSeconds());
        assertFalse(tcpCfg.getMembers().isEmpty());
        assertEquals(3, tcpCfg.getMembers().size());
        assertTrue(tcpCfg.getMembers().contains("192.168.0.1"));
        assertTrue(tcpCfg.getMembers().contains("192.168.0.2"));
        assertTrue(tcpCfg.getMembers().contains("192.168.0.3"));
        assertEquals("127.0.0.1", tcpCfg.getRequiredMember());

        // Interfaces Config
        final InterfacesConfig ifacesCfg = netCfg.getInterfaces();
        assertEquals(false, ifacesCfg.isEnabled());
        assertEquals(3, ifacesCfg.getInterfaces().size());
        assertTrue(ifacesCfg.getInterfaces().contains("10.3.16.*"));
        assertTrue(ifacesCfg.getInterfaces().contains("10.3.10.4-18"));
        assertTrue(ifacesCfg.getInterfaces().contains("192.168.1.3"));
    }

    // *************************************************************************
    //
    // *************************************************************************

    private InputStream getResource() {
        InputStream in = YamlConfigTest.class.getResourceAsStream(RESOURCE);
        if (in == null) {
            throw new IllegalArgumentException("Specified resource '" + RESOURCE + "' could not be found!");
        }

        return in;
    }
}
