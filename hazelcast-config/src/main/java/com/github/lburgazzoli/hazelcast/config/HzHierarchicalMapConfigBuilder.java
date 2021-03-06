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
import com.hazelcast.config.MapConfig;

import java.util.Map;

public abstract class HzHierarchicalMapConfigBuilder implements ConfigBuilder {

    @SuppressWarnings("unchecked")
    protected Config process(final Config config, Map<String,Object> root) throws Exception {
        root.entrySet().stream().forEach(
            entry -> {
                switch (entry.getKey()) {
                    case HzConfig.Elements.GROUP:
                        HzConfig.forEachElementApply(
                            config::getGroupConfig,
                            entry,
                            HzConfigProcessors.GROUP);
                        break;
                    case HzConfig.Elements.NETWORK:
                        HzConfig.forEachElementApply(
                            config::getNetworkConfig,
                            entry,
                            HzConfigProcessors.NETWORK);
                        break;
                    case HzConfig.Elements.MAPS + "_":
                        HzConfig.valueAsListOfMaps(entry).stream().map(
                            map ->
                                HzConfig.forEachElementApply(
                                    MapConfig::new,
                                    map,
                                    HzConfigProcessors.MAP)
                        ).forEach(config::addMapConfig);
                        break;
                }
            }
        );

        return config;
    }
}
