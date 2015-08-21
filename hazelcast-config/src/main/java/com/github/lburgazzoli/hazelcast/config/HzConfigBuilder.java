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

package com.github.lburgazzoli.hazelcast.config;

import com.hazelcast.config.Config;
import com.hazelcast.config.ConfigBuilder;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.MulticastConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.config.TcpIpConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static com.github.lburgazzoli.hazelcast.config.HzConfig.convert;
import static com.github.lburgazzoli.hazelcast.config.HzConfig.forEachElementApply;

public abstract class HzConfigBuilder implements ConfigBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(HzConfigBuilder.class);

    // *************************************************************************
    //
    // *************************************************************************

    @SuppressWarnings("unchecked")
    protected Config process(final Config config, Map<String,Object> root) throws Exception {
        root.entrySet().stream().forEach(
            entry -> {
                switch (entry.getKey()) {
                    case HzConfig.Elements.GROUP:
                        forEachElementApply(config::getGroupConfig, entry, this::groupProcessor);
                        break;
                    case HzConfig.Elements.NETWORK:
                        forEachElementApply(config::getNetworkConfig, entry, this::networkConfigProcessor);
                        break;
                }
            }
        );

        return config;
    }

    // *************************************************************************
    //
    // *************************************************************************

    private GroupConfig groupProcessor(GroupConfig config, String key, Object value) {
        switch(key) {
            case "name":
                config.setName(convert(value, String.class));
                break;
            case "password":
                config.setPassword(convert(value, String.class));
                break;
        }

        return config;
    }

    private NetworkConfig networkConfigProcessor(NetworkConfig config, String key, Object value) {
        switch(key) {
            case "reuse-address":
                config.setReuseAddress(convert(value, Boolean.class));
                break;
            case "port":
                config.setPort(convert(value, Integer.class));
                break;
            case "port-auto-increment":
                config.setPortAutoIncrement(convert(value, Boolean.class));
                break;
            case "port-count":
                config.setPortCount(convert(value, Integer.class));
                break;
            case "public-address":
                config.setPublicAddress(convert(value, String.class));
                break;
            case "outbound-ports":
                ((List<String>) value).forEach(config::addOutboundPortDefinition);
                break;
            case "join":
                forEachElementApply(config::getJoin, value, this::joinConfigProcessor);
                break;
        }

        return config;
    }

    private JoinConfig joinConfigProcessor(JoinConfig config, String key, Object value) {
        switch(key) {
            case "multicast":
                forEachElementApply(
                    config::getMulticastConfig,
                    value,
                    this::multicastConfigProcessor);

            case "tcp-ip":
                forEachElementApply(
                    config::getTcpIpConfig,
                    value,
                    this::tcpIpConfigProcessor);
        }

        return config;
    }

    private MulticastConfig multicastConfigProcessor(MulticastConfig config, String key, Object value) {
        switch(key) {
            case "enabled":
                config.setEnabled(convert(value, Boolean.class));
                break;
            case "loopback-mode-enabled":
                config.setLoopbackModeEnabled(convert(value, Boolean.class));
                break;
            case "multicast-group":
                config.setMulticastGroup(convert(value, String.class));
                break;
            case "multicast-port":
                config.setMulticastPort(convert(value, Integer.class));
                break;
            case "multicast-timeout-seconds":
                config.setMulticastTimeoutSeconds(convert(value, Integer.class));
                break;
            case "multicast-time-to-live-seconds":
                config.setMulticastTimeToLive(convert(value, Integer.class));
                break;
            case "multicast-time-to-live":
                config.setMulticastTimeToLive(convert(value, Integer.class));
                break;
            case "trusted-interfaces":
                ((List<String>)value).forEach(config::addTrustedInterface);
                break;
        }

        return config;
    }

    private TcpIpConfig tcpIpConfigProcessor(TcpIpConfig config, String key, Object value) {
        switch(key) {
            case "enabled":
                config.setEnabled(convert(value, Boolean.class));
                break;
            case "connection-timeout-seconds":
                config.setConnectionTimeoutSeconds(convert(value, Integer.class));
                break;
        }

        return config;
    }
}
