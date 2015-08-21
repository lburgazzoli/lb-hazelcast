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

import java.util.Map;

public class HzServiceRegistry {
    private final HazelcastInstance instance;
    private final Map<String,HzServiceDefinition> svcMap;
    private final ILock svcLock;

    /**
     * c-tor
     *
     * @param instance
     * @param registryName
     */
    public HzServiceRegistry(final HazelcastInstance instance, String registryName) {
        this.instance = instance;
        this.svcMap   = this.instance.getMap(registryName);
        this.svcLock  = this.instance.getLock(registryName + ":lock");
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
