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
package com.github.lburgazzoli.hazelcast.discovery.dns;

import com.hazelcast.nio.Address;
import com.hazelcast.spi.discovery.DiscoveredNode;
import com.hazelcast.spi.discovery.DiscoveryMode;
import com.hazelcast.spi.discovery.DiscoveryStrategy;
import com.hazelcast.spi.discovery.SimpleDiscoveredNode;
import com.spotify.dns.DnsSrvResolver;
import com.spotify.dns.DnsSrvResolvers;
import com.spotify.dns.LookupResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;

public class DnsDiscoveryStrategy implements DiscoveryStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(DnsDiscoveryStrategy.class);

    private final String serviceName;

    private DnsSrvResolver resolver;

    public DnsDiscoveryStrategy(final Map<String, Comparable> properties) {
        this.serviceName = (String) properties.get(DnsDiscovery.PROPERTY_SERVICE_NAME.key());
        if(this.serviceName == null) {
            throw new RuntimeException("Property 'serviceName' is missing in the DNS provider");
        }
    }


    @Override
    public void start(DiscoveryMode discoveryMode) {
        this.resolver = DnsSrvResolvers.newBuilder()
            .cachingLookups(true)
            .retainingDataOnFailures(true)
            .dnsLookupTimeoutMillis(1000)
            .build();
    }

    @Override
    public Collection<DiscoveredNode> discoverNodes() {
        final Properties empty = new Properties();
        final Collection<DiscoveredNode> list = new LinkedList<>();

        if(this.resolver != null) {
            resolver.resolve(this.serviceName).stream()
                .map(new Result2Address())
                .map(address -> new SimpleDiscoveredNode(address))
                .forEach(list::add);
        }

        return list;
    }

    @Override
    public void destroy() {
        this.resolver = null;
    }

    // *************************************************************************
    //
    // *************************************************************************

    private final class Result2Address implements Function<LookupResult, Address> {
        @Override
        public Address apply(LookupResult result) {
            try {
                return new Address(result.host(), result.port());
            } catch(Exception e) {
                LOGGER.warn("", e);
            }
            return null;
        }
    }
}
