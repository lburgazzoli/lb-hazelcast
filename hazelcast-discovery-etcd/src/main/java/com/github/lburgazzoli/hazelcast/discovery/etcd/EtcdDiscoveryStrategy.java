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

import com.hazelcast.spi.discovery.DiscoveredNode;
import com.hazelcast.spi.discovery.DiscoveryMode;
import com.hazelcast.spi.discovery.DiscoveryStrategy;
import com.hazelcast.util.ExceptionUtil;
import mousio.etcd4j.EtcdClient;
import mousio.etcd4j.responses.EtcdKeysResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class EtcdDiscoveryStrategy implements DiscoveryStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(EtcdDiscoveryStrategy.class);

    private final String[] etcdUrls;
    private final String serviceName;

    private EtcdClient client;

    public EtcdDiscoveryStrategy(final Map<String, Comparable> properties) {
        this.client = null;

        this.etcdUrls = ((String) properties.getOrDefault(
            EtcdDiscovery.PROPERTY_URLS.key(),
            EtcdDiscovery.DEFAULT_ETCD_URLS))
                .split(EtcdDiscovery.URLS_SEPARATOR);
        this.serviceName = (String) properties.getOrDefault(
            EtcdDiscovery.PROPERTY_SERVICE_NAME.key(),
            EtcdDiscovery.DEFAULT_SERVICE_NAME);
    }

    @Override
    public void start(DiscoveryMode discoveryMode) {
        final URI[] uris = new URI[etcdUrls.length];
        for(int i=0; i<uris.length; i++) {
            uris[i] = URI.create(etcdUrls[i]);
        }

        this.client = new EtcdClient(uris);
    }

    @Override
    public Collection<DiscoveredNode> discoverNodes() {
        final Collection<DiscoveredNode> list = new LinkedList<>();

        if(this.client != null) {
            try {
                final EtcdKeysResponse response = client.getDir(this.serviceName)
                    .recursive()
                    .timeout(1, TimeUnit.SECONDS)
                    .send()
                    .get();

                if(response.node != null) {
                    response.node.nodes.stream()
                        .map(node -> node.value)
                        .filter(StringUtils::isNotBlank)
                        .map(new Node2Address())
                        .filter(Objects::nonNull)
                        .forEach(list::add);
                }
            } catch (Exception e) {
                LOGGER.warn("", e);
                throw ExceptionUtil.rethrow(e);
            }
        }

        return list;
    }

    @Override
    public void destroy() {
        try {
            this.client.close();
        } catch(IOException e) {
            LOGGER.warn("", e);
        }

        this.client = null;
    }

    // *************************************************************************
    //
    // *************************************************************************

    private static final class Node2Address
            implements Function<String, EtcdDiscoveryNode> {

        @Override
        public EtcdDiscoveryNode apply(String value) {
            try {
                return EtcdDiscovery.MAPPER.readValue(value, EtcdDiscoveryNode.class);
            } catch(Exception e) {
                LOGGER.warn("", e);
            }

            return null;
        }
    }
}
