lb-hazelcast-discovery-etcd
===========================

Register Hazelcast services:

```
etcdctl set /hazelcast/node1' {"host":"127.0.0.1","port":9001}'
etcdctl set /hazelcast/node2' {"host":"127.0.0.1","port":9002}'
etcdctl set /hazelcast/node3' {"host":"127.0.0.1","port":9003}'
```

Configuration:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<hazelcast xsi:schemaLocation="http://www.hazelcast.com/schema/config hazelcast-config-3.5.xsd"
           xmlns="http://www.hazelcast.com/schema/config"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <properties>
        <property name="hazelcast.discovery.enabled">true</property>
        <property name="hazelcast.memcache.enabled">false</property>
        <property name="hazelcast.rest.enabled">false</property>
        <property name="hazelcast.logging.type">slf4j</property>
    </properties>

    <network>
        <join>
            <multicast enabled="false"/>
            <tcp-ip enabled="false"/>
            <aws enabled="false"/>

            <discovery-providers>
                <discovery-provider enabled="true" class="com.github.lburgazzoli.hazelcast.discovery.etcd.EtcdDiscoveryProvider">
                    <properties>
                        <property name ="urls">http://localhost:4001</property>
                        <property name ="serviceName">hazelcast</property>
                    </properties>
                </discovery-provider>
            </discovery-providers>
        </join>
    </network>

</hazelcast>
```
