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
package com.github.lburgazzoli.hazelcast.discovery.etcd

class EtcdDiscoveryTestSupport {
    public static final def NODES = [
        "node1" : [
            "host": "127.0.0.1",
            "port": 9001,
            "tags": [
                "name": "node1"
            ]
        ],
        "node2" : [
            "host": "127.0.0.1",
            "port": 9002,
            "tags": [
                "name": "node2"
            ]
        ],
        "node3" : [
            "host": "127.0.0.1",
            "port": EtcdDiscovery.DEFAULT_HZ_PORT,
            "tags": [
                "name": "node3"
            ]
        ],
        "node4" : [
            "host": "127.0.0.1",
            "tags": [
                "name": "node4"
            ]
        ]
    ]
}
