/*
 * Copyright 2014 lburgazzoli
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
package com.github.lburgazzoli.hazelcast;

import com.google.common.collect.Maps;

import java.util.Map;

public class HzServiceDefinition {
    private final Map<String,String> m_attributes;

    public HzServiceDefinition() {
        m_attributes = Maps.newHashMap();
    }

    public void put(String key, String val) {
        m_attributes.put(key, val);
    }

    public String get(String key) {
        return m_attributes.get(key);
    }
}
