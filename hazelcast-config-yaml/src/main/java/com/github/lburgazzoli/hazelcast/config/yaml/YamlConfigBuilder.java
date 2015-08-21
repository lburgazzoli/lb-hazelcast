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

import com.github.lburgazzoli.hazelcast.config.HzConfigBuilder;
import com.hazelcast.config.Config;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class YamlConfigBuilder extends HzConfigBuilder {

    private InputStream in;

    public YamlConfigBuilder(InputStream inputStream) {
        if (inputStream == null) {
            throw new IllegalArgumentException("inputStream can't be null");
        }

        this.in = inputStream;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Config build() {
        try {
            Yaml yaml = new Yaml();

            return process(
                new Config(),
                (Map<String,Object>)yaml.load(this.in)
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
