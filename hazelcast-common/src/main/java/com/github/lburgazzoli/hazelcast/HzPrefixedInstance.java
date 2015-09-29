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

import com.google.common.collect.Lists;
import com.hazelcast.config.Config;
import com.hazelcast.core.*;
import com.hazelcast.logging.LoggingService;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.quorum.QuorumService;
import com.hazelcast.ringbuffer.Ringbuffer;
import com.hazelcast.transaction.*;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.concurrent.ConcurrentMap;

public class HzPrefixedInstance implements HazelcastInstance {
    private final HazelcastInstance instance;
    private final String prefix;

    /**
     * c-tor
     *
     * @param instance the Hazelcast instance
     */
    public HzPrefixedInstance(final HazelcastInstance instance) {
        this(instance, null);
    }

    /**
     * c-tor
     *
     * @param instance  the Hazelcast instance
     * @param prefix    the object prefix
     */
    public HzPrefixedInstance(final HazelcastInstance instance, final String prefix) {
        this.instance = instance;
        this.prefix = prefix;
    }

    /**
     * c-tor
     *
     * @param config the Hazelcast config
     */
    public HzPrefixedInstance(final Config config) {
        this(Hazelcast.newHazelcastInstance(config));
    }

    /**
     * c-tor
     *
     * @param config    the Hazelcast config
     * @param prefix    the object prefix
     */
    public HzPrefixedInstance(final Config config, String prefix) {
        this(Hazelcast.newHazelcastInstance(config), prefix);
    }

    // *************************************************************************
    //
    // *************************************************************************

    /**
     *
     * @param name the name to prefix
     * @return the prefix
     */
    public String getPrefix(String name) {
        return StringUtils.isBlank(prefix)
            ? name
            : prefix + ":" + name;
    }

    // *************************************************************************
    //
    // *************************************************************************

    @Override
    public String getName() {
        return instance.getName();
    }

    @Override
    public <E> IQueue<E> getQueue(String name) {
        return instance.getQueue(getPrefix(name));
    }

    @Override
    public <E> ITopic<E> getTopic(String name) {
        return instance.getTopic(getPrefix(name));
    }

    @Override
    public <E> ISet<E> getSet(String name) {
        return instance.getSet(getPrefix(name));
    }

    @Override
    public <E> IList<E> getList(String name) {
        return instance.getList(getPrefix(name));
    }

    @Override
    public <K, V> IMap<K, V> getMap(String name) {
        return instance.getMap(getPrefix(name));
    }

    @Override
    public <K, V> ReplicatedMap<K, V> getReplicatedMap(String name) {
        return instance.getReplicatedMap(getPrefix(name));
    }

    @Override
    public JobTracker getJobTracker(String name) {
        return instance.getJobTracker(getPrefix(name));
    }

    @Override
    public <K, V> MultiMap<K, V> getMultiMap(String name) {
        return instance.getMultiMap(getPrefix(name));
    }

    @Override
    public ILock getLock(String name) {
        return instance.getLock(getPrefix(name));
    }

    @Override
    public <E> Ringbuffer<E> getRingbuffer(String name) {
        return instance.getRingbuffer(getPrefix(name));
    }

    @Override
    public <E> ITopic<E> getReliableTopic(String name) {
        return instance.getReliableTopic(getPrefix(name));
    }

    @Override
    public Cluster getCluster() {
        return instance.getCluster();
    }

    @Override
    public Endpoint getLocalEndpoint() {
        return instance.getLocalEndpoint();
    }

    @Override
    public IExecutorService getExecutorService(String name) {
        return instance.getExecutorService(getPrefix(name));
    }

    @Override
    public <T> T executeTransaction(TransactionalTask<T> task) throws TransactionException {
        return instance.executeTransaction(task);
    }

    @Override
    public <T> T executeTransaction(TransactionOptions options, TransactionalTask<T> task) throws TransactionException {
        return instance.executeTransaction(options, task);
    }

    @Override
    public TransactionContext newTransactionContext() {
        return instance.newTransactionContext();
    }

    @Override
    public TransactionContext newTransactionContext(TransactionOptions options) {
        return instance.newTransactionContext(options);
    }

    @Override
    public IdGenerator getIdGenerator(String name) {
        return instance.getIdGenerator(getPrefix(name));
    }

    @Override
    public IAtomicLong getAtomicLong(String name) {
        return instance.getAtomicLong(getPrefix(name));
    }

    @Override
    public <E> IAtomicReference<E> getAtomicReference(String name) {
        return instance.getAtomicReference(getPrefix(name));
    }

    @Override
    public ICountDownLatch getCountDownLatch(String name) {
        return instance.getCountDownLatch(getPrefix(name));
    }

    @Override
    public ISemaphore getSemaphore(String name) {
        return instance.getSemaphore(getPrefix(name));
    }

    @Override
    public Collection<DistributedObject> getDistributedObjects() {
        return instance.getDistributedObjects();
    }

    @Override
    public String addDistributedObjectListener(DistributedObjectListener distributedObjectListener) {
        return instance.addDistributedObjectListener(distributedObjectListener);
    }

    @Override
    public boolean removeDistributedObjectListener(String registrationId) {
        return instance.removeDistributedObjectListener(registrationId);
    }

    @Override
    public Config getConfig() {
        return instance.getConfig();
    }

    @Override
    public PartitionService getPartitionService() {
        return instance.getPartitionService();
    }

    @Override
    public QuorumService getQuorumService() {
        return instance.getQuorumService();
    }

    @Override
    public ClientService getClientService() {
        return instance.getClientService();
    }

    @Override
    public LoggingService getLoggingService() {
        return instance.getLoggingService();
    }

    @Override
    public LifecycleService getLifecycleService() {
        return instance.getLifecycleService();
    }

    @Override
    public <T extends DistributedObject> T getDistributedObject(String serviceName, String name) {
        return instance.getDistributedObject(serviceName, getPrefix(name));
    }

    @Override
    public ConcurrentMap<String, Object> getUserContext() {
        return instance.getUserContext();
    }

    @Override
    public HazelcastXAResource getXAResource() {
        return instance.getXAResource();
    }

    @Override
    public void shutdown() {
        instance.shutdown();
    }

    // *************************************************************************
    // Helpers
    // *************************************************************************

    /**
     *
     * @param type  the distributed object type
     * @return      the distributed objects
     */
    public Collection<DistributedObject> getDistributedObjects(final Class<?> type) {
        Collection<DistributedObject> rv = Lists.newArrayList();
        for(DistributedObject object : instance.getDistributedObjects()) {
            if(type == null) {
                rv.add(object);
            } else if(type.isAssignableFrom(object.getClass())) {
                rv.add(object);
            }
        }

        return  rv;
    }
}

