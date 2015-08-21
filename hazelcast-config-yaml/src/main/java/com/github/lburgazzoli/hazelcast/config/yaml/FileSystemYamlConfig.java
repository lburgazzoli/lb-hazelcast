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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

public class FileSystemYamlConfig extends Config {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileSystemYamlConfig.class);


    public FileSystemYamlConfig(String configFilename) throws FileNotFoundException {
        this(configFilename, System.getProperties());
    }

    public FileSystemYamlConfig(String configFilename, Properties properties) throws FileNotFoundException {
        this(new File(configFilename), properties);
    }

    public FileSystemYamlConfig(File configFile) throws FileNotFoundException {
        this(configFile, System.getProperties());
    }

    public FileSystemYamlConfig(File configFile, Properties properties) throws FileNotFoundException {
        if (configFile == null) {
            throw new IllegalArgumentException("configFile can't be null");
        }

        if (properties == null) {
            throw new IllegalArgumentException("properties can't be null");
        }

        LOGGER.info("Configuring Hazelcast from '" + configFile.getAbsolutePath() + "'.");
        new YamlConfigBuilder(new FileInputStream(configFile)).build(this);
    }
}
