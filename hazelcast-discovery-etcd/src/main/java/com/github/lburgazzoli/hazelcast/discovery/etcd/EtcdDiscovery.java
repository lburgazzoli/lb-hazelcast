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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import com.hazelcast.config.properties.PropertyDefinition;
import com.hazelcast.config.properties.PropertyTypeConverter;
import com.hazelcast.config.properties.SimplePropertyDefinition;

import java.util.Properties;

public class EtcdDiscovery {
    public static final ObjectMapper MAPPER =
        new ObjectMapper()
            .registerModule(new AfterburnerModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    public static final int    DEFAULT_HZ_PORT      = 5701;
    public static final int    DEFAULT_ETCD_PORT    = 4001;
    public static final String DEFAULT_ETCD_URL     = "http://localhost:4001";
    public static final String DEFAULT_ETCD_URLS    = "http://localhost:2379,http://localhost:4001";
    public static final String DEFAULT_SERVICE_NAME = "_hazelcast";
    public static final String CONFIG_KEY_HOST      = "host";
    public static final String CONFIG_KEY_PORT      = "port";
    public static final String CONFIG_KEY_TAGS      = "tags";
    public static final String URLS_SEPARATOR       = ",";

    public static final Properties NO_PROPERTIES = new Properties();

    public static final PropertyDefinition PROPERTY_URLS =
        new SimplePropertyDefinition("urls", PropertyTypeConverter.STRING);

    public static final PropertyDefinition PROPERTY_SERVICE_NAME =
        new SimplePropertyDefinition("serviceName", PropertyTypeConverter.STRING);
}
