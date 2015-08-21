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

package com.github.lburgazzoli.hazelcast.config.json;

import com.hazelcast.config.Config;
import com.hazelcast.config.MulticastConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.config.TcpIpConfig;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.*;

public class JsonConfigTest {
    public static final String RESOURCE = "/hazelcast/hazelcast.json";

    @Test
    public void testBuilder() {
        final Config config = new JsonConfigBuilder(getResource()).build();
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
        assertTrue(netCfg.getOutboundPortDefinitions().contains("10100"));
        assertTrue(netCfg.getOutboundPortDefinitions().contains("9000-10000"));
        assertFalse(netCfg.getOutboundPortDefinitions().contains("1"));
        assertEquals("127.0.0.1", netCfg.getPublicAddress());

        // Multicast Config
        final MulticastConfig mcastCfg = netCfg.getJoin().getMulticastConfig();
        assertEquals(false, mcastCfg.isEnabled());
        assertEquals(false, mcastCfg.isLoopbackModeEnabled());
        assertTrue(mcastCfg.getTrustedInterfaces().contains("eth0"));
        assertTrue(mcastCfg.getTrustedInterfaces().contains("eth1"));
        assertFalse(mcastCfg.getTrustedInterfaces().contains("lo0"));

        // TcpIp Config
        final TcpIpConfig tcpCfg = netCfg.getJoin().getTcpIpConfig();
        assertEquals(false, tcpCfg.isEnabled());
        assertEquals(10, tcpCfg.getConnectionTimeoutSeconds());
    }

    // *************************************************************************
    //
    // *************************************************************************

    private InputStream getResource() {
        InputStream in = JsonConfigTest.class.getResourceAsStream(RESOURCE);
        if (in == null) {
            throw new IllegalArgumentException("Specified resource '" + RESOURCE + "' could not be found!");
        }

        return in;
    }
}
