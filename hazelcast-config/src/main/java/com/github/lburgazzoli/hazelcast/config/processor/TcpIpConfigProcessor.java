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

package com.github.lburgazzoli.hazelcast.config.processor;

import com.github.lburgazzoli.hazelcast.config.HzConfig;
import com.github.lburgazzoli.hazelcast.config.HzConfigProcessor;
import com.hazelcast.config.TcpIpConfig;

import java.util.List;

public class TcpIpConfigProcessor implements HzConfigProcessor<TcpIpConfig> {
    public static final TcpIpConfigProcessor INSTANCE = new TcpIpConfigProcessor();

    @SuppressWarnings("unchecked")
    @Override
    public TcpIpConfig apply(TcpIpConfig config, String key, Object value) {
        switch(key) {
            case "members":
                ((List<String>)value).forEach(config::addMember);
                break;
            default:
                HzConfig.setPropertyValue(config, key, value);
                break;
        }

        return config;
    }
}
