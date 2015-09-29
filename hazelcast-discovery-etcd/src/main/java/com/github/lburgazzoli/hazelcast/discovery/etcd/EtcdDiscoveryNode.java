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
package com.github.lburgazzoli.hazelcast.discovery.etcd;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Maps;
import com.hazelcast.nio.Address;
import com.hazelcast.spi.discovery.DiscoveredNode;

import java.net.UnknownHostException;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
class EtcdDiscoveryNode implements DiscoveredNode {

    private final Address address;
    private final Map<String, Object> properties;

    public EtcdDiscoveryNode(
            @JsonProperty("host") final String host,
            @JsonProperty("port") final Integer port,
            @JsonProperty("tags") final Map<String,String> tags)
            throws UnknownHostException
    {
        this.address = new Address(host, port != null ? port : EtcdDiscovery.DEFAULT_HZ_PORT);
        this.properties = Maps.newHashMap();

        if(tags != null) {
            this.properties.putAll(tags);
        }
    }

    @Override
    public Address getPrivateAddress() {
        return this.address;
    }

    @Override
    public Address getPublicAddress() {
        return this.address;
    }

    @Override
    public Map<String, Object> getProperties() {
        return this.properties;
    }
}
