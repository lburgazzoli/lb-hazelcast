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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

public class ClasspathYamlConfig extends Config {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClasspathYamlConfig.class);

    public ClasspathYamlConfig(String resource) {
        this(resource, System.getProperties());
    }

    public ClasspathYamlConfig(String resource, Properties properties) {
        this(Thread.currentThread().getContextClassLoader(), resource, properties);
    }

    public ClasspathYamlConfig(ClassLoader classLoader, String resource) {
        this(classLoader, resource, System.getProperties());
    }

    public ClasspathYamlConfig(ClassLoader classLoader, String resource, Properties properties) {
        if (classLoader == null) {
            throw new IllegalArgumentException("classLoader can't be null");
        }

        if (resource == null) {
            throw new IllegalArgumentException("resource can't be null");
        }

        if (properties == null) {
            throw new IllegalArgumentException("properties can't be null");
        }

        LOGGER.info("Configuring Hazelcast from '" + resource + "'.");
        InputStream in = classLoader.getResourceAsStream(resource);
        if (in == null) {
            throw new IllegalArgumentException("Specified resource '" + resource + "' could not be found!");
        }

        new YamlConfigBuilder(in).build(this);
    }
}
