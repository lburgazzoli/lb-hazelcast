#!/usr/bin/env bash

./etcd-dist/etcd --name 'lb-hazelcast-discovery' --data-dir /tmp/lb-hazelcast-discovery &

sleep 5

./etcd-dist/etcdctl set /_hazelcast/node1 '{ "host": "127.0.0.1", "port": 9001 , "tags": { "name": "node1" } }'
./etcd-dist/etcdctl set /_hazelcast/node2 '{ "host": "127.0.0.1", "port": 9002 , "tags": { "name": "node2" } }'
./etcd-dist/etcdctl set /_hazelcast/node3 '{ "host": "127.0.0.1", "port": 5701 , "tags": { "name": "node3" } }'
./etcd-dist/etcdctl set /_hazelcast/node4 '{ "host": "127.0.0.1" , "tags": { "name": "node4" } }'

