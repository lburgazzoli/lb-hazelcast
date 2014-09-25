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

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ILock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class HzServiceRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(HzServiceRegistry.class);

    private final HazelcastInstance m_hz;
    private final Map<String,HzServiceDefinition> m_svcMap;
    private final ILock m_svcLock;

    /**
     * c-tor
     *
     * @param hz
     * @param registryName
     */
    public HzServiceRegistry(final HazelcastInstance hz, String registryName) {
        m_hz      = hz;
        m_svcMap  = m_hz.getMap(registryName);
        m_svcLock = m_hz.getLock(registryName + ":lock");
    }

    // *************************************************************************
    //
    // *************************************************************************

    public void add() {
    }

    public HzServiceRegistry lookup() {
        return null;
    }

    public HzServiceRegistry remove() {
        return null;
    }
}
