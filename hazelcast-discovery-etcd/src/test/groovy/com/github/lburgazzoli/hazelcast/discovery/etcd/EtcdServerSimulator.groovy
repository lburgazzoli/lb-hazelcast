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

import spark.ResponseTransformer
import spark.Route

import static spark.Spark.get
import static spark.Spark.awaitInitialization
import static spark.Spark.port
import static spark.Spark.stop

class EtcdServerSimulator {

    static void setUp() {
        port(EtcdDiscovery.DEFAULT_ETCD_PORT);
        get("/v2/keys/${EtcdDiscovery.DEFAULT_SERVICE_NAME}", {
            request, response -> [
                action : 'get',
                node   : [
                    key   : "/${EtcdDiscovery.DEFAULT_SERVICE_NAME}",
                    nodes : EtcdDiscoveryTestSupport.NODES.collect {
                        [
                            key  : "${EtcdDiscovery.DEFAULT_SERVICE_NAME}/${it.key}",
                            value: EtcdDiscovery.MAPPER.writeValueAsString(it.value)
                        ] as SparkEtcdNode
                    }
                ] as SparkEtcdNode,
            ] as SparkEtcdResponse
        } as Route, {
            object -> EtcdDiscovery.MAPPER.writeValueAsString(object)
        } as ResponseTransformer
        )

        awaitInitialization()
    }

    static void tearDown() {
        stop()
    }

    // *************************************************************************
    //
    // *************************************************************************

    public static class SparkEtcdResponse {
        String action
        SparkEtcdNode node
    }

    public static class SparkEtcdNode {
        String key;
        String value;
        Long createdIndex;
        Long modifiedIndex;
        Long ttl;
        List<SparkEtcdNode> nodes;
    }
}
