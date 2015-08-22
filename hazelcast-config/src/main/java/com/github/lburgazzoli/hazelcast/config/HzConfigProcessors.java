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

import com.hazelcast.config.AwsConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.config.InterfacesConfig;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MulticastConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.config.TcpIpConfig;

import java.util.List;

import static com.github.lburgazzoli.hazelcast.config.HzConfig.forEachElementApply;

public class HzConfigProcessors {

    // *************************************************************************
    // Group
    // *************************************************************************

    static final HzConfigProcessor<GroupConfig> GROUP =
        (GroupConfig config, String key, Object value) ->
            HzConfig.setPropertyValue(config, key, value);

    // *************************************************************************
    // Network
    // *************************************************************************

    static final HzConfigProcessor<NetworkConfig> NETWORK =
        (NetworkConfig config, String key, Object value) -> {
            switch(key) {
                case "outbound-ports":
                    ((List<String>) value).forEach(config::addOutboundPortDefinition);
                    break;
                case "join":
                    forEachElementApply(config::getJoin, value, HzConfigProcessors.JOIN);
                    break;
                case "interfaces":
                    forEachElementApply(config::getInterfaces, value, HzConfigProcessors.INTERFACES);
                    break;
                default:
                    HzConfig.setPropertyValue(config, key, value);
                    break;
            }

            return config;
        };

    static final HzConfigProcessor<JoinConfig> JOIN =
        (JoinConfig config, String key, Object value) -> {
            switch(key) {
                case "multicast":
                    forEachElementApply(
                        config::getMulticastConfig,
                        value,
                        HzConfigProcessors.MULTICAST);
                    break;
                case "tcp-ip":
                    forEachElementApply(
                        config::getTcpIpConfig,
                        value,
                        HzConfigProcessors.TCPIP);
                    break;
                case "aws":
                    forEachElementApply(
                        config::getAwsConfig,
                        value,
                        HzConfigProcessors.AWS);
                    break;
            }

            config.verify();
            return config;
        };

    static final HzConfigProcessor<MulticastConfig> MULTICAST =
        (MulticastConfig config, String key, Object value) -> {
            switch(key) {
                case "trusted-interfaces":
                    ((List<String>)value).forEach(config::addTrustedInterface);
                    break;
                default:
                    HzConfig.setPropertyValue(config, key, value);
                    break;
            }

            return config;
        };

    static final HzConfigProcessor<TcpIpConfig> TCPIP =
        (TcpIpConfig config, String key, Object value) -> {
            switch(key) {
                case "members":
                    ((List<String>)value).forEach(config::addMember);
                    break;
                default:
                    HzConfig.setPropertyValue(config, key, value);
                    break;
            }

            return config;
        };

    static final HzConfigProcessor<AwsConfig> AWS =
        (AwsConfig config, String key, Object value) -> {
            switch(key) {
                default:
                    HzConfig.setPropertyValue(config, key, value);
                    break;
            }

            return config;
        };

    static final HzConfigProcessor<InterfacesConfig> INTERFACES =
        (InterfacesConfig config, String key, Object value) -> {
            switch(key) {
                case "addresses":
                    ((List<String>)value).forEach(config::addInterface);
                    break;
                default:
                    HzConfig.setPropertyValue(config, key, value);
                    break;
            }

            return config;
        };

    // *************************************************************************
    // Map
    // *************************************************************************

    static final HzConfigProcessor<MapConfig> MAP =
        (MapConfig config, String key, Object value) -> {
            switch(key) {
                default:
                    HzConfig.setPropertyValue(config, key, value);
                    break;
            }

            return config;
        };
}
