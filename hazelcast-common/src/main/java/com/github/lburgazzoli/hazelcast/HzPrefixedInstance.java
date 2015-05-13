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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.concurrent.ConcurrentMap;

public class HzPrefixedInstance implements HazelcastInstance {
    private static final Logger LOGGER = LoggerFactory.getLogger(HzPrefixedInstance.class);

    private final HazelcastInstance m_instance;
    private final String m_prefix;

    /**
     * c-tor
     *
     * @param instance the Hazelcast instance
     */
    public HzPrefixedInstance(final HazelcastInstance instance) {
        this(instance,null);
    }
    /**
     * c-tor
     *
     * @param instance  the Hazelcast instance
     * @param prefix    the object prefix
     */
    public HzPrefixedInstance(final HazelcastInstance instance, final String prefix) {
        m_instance = instance;
        m_prefix = prefix;
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
        this(Hazelcast.newHazelcastInstance(config),prefix);
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
        return StringUtils.isEmpty(m_prefix)
            ? name
            : m_prefix + ":" + name;
    }

    // *************************************************************************
    //
    // *************************************************************************

    @Override
    public String getName() {
        return m_instance.getName();
    }

    @Override
    public <E> IQueue<E> getQueue(String name) {
        return m_instance.getQueue(getPrefix(name));
    }

    @Override
    public <E> ITopic<E> getTopic(String name) {
        return m_instance.getTopic(getPrefix(name));
    }

    @Override
    public <E> ISet<E> getSet(String name) {
        return m_instance.getSet(getPrefix(name));
    }

    @Override
    public <E> IList<E> getList(String name) {
        return m_instance.getList(getPrefix(name));
    }

    @Override
    public <K, V> IMap<K, V> getMap(String name) {
        return m_instance.getMap(getPrefix(name));
    }

    @Override
    public <K, V> ReplicatedMap<K, V> getReplicatedMap(String name) {
        return m_instance.getReplicatedMap(getPrefix(name));
    }

    @Override
    public JobTracker getJobTracker(String name) {
        return m_instance.getJobTracker(getPrefix(name));
    }

    @Override
    public <K, V> MultiMap<K, V> getMultiMap(String name) {
        return m_instance.getMultiMap(getPrefix(name));
    }

    @Override
    public ILock getLock(String name) {
        return m_instance.getLock(getPrefix(name));
    }

    @Deprecated
    @Override
    public ILock getLock(Object key) {
        return m_instance.getLock(key);
    }

    @Override
    public <E> Ringbuffer<E> getRingbuffer(String name) {
        return m_instance.getRingbuffer(getPrefix(name));
    }

    @Override
    public <E> ITopic<E> getReliableTopic(String name) {
        return m_instance.getReliableTopic(getPrefix(name));
    }

    @Override
    public Cluster getCluster() {
        return m_instance.getCluster();
    }

    @Override
    public Endpoint getLocalEndpoint() {
        return m_instance.getLocalEndpoint();
    }

    @Override
    public IExecutorService getExecutorService(String name) {
        return m_instance.getExecutorService(getPrefix(name));
    }

    @Override
    public <T> T executeTransaction(TransactionalTask<T> task) throws TransactionException {
        return m_instance.executeTransaction(task);
    }

    @Override
    public <T> T executeTransaction(TransactionOptions options, TransactionalTask<T> task) throws TransactionException {
        return m_instance.executeTransaction(options, task);
    }

    @Override
    public TransactionContext newTransactionContext() {
        return m_instance.newTransactionContext();
    }

    @Override
    public TransactionContext newTransactionContext(TransactionOptions options) {
        return m_instance.newTransactionContext(options);
    }

    @Override
    public IdGenerator getIdGenerator(String name) {
        return m_instance.getIdGenerator(getPrefix(name));
    }

    @Override
    public IAtomicLong getAtomicLong(String name) {
        return m_instance.getAtomicLong(getPrefix(name));
    }

    @Override
    public <E> IAtomicReference<E> getAtomicReference(String name) {
        return m_instance.getAtomicReference(getPrefix(name));
    }

    @Override
    public ICountDownLatch getCountDownLatch(String name) {
        return m_instance.getCountDownLatch(getPrefix(name));
    }

    @Override
    public ISemaphore getSemaphore(String name) {
        return m_instance.getSemaphore(getPrefix(name));
    }

    @Override
    public Collection<DistributedObject> getDistributedObjects() {
        return m_instance.getDistributedObjects();
    }

    @Override
    public String addDistributedObjectListener(DistributedObjectListener distributedObjectListener) {
        return m_instance.addDistributedObjectListener(distributedObjectListener);
    }

    @Override
    public boolean removeDistributedObjectListener(String registrationId) {
        return m_instance.removeDistributedObjectListener(registrationId);
    }

    @Override
    public Config getConfig() {
        return m_instance.getConfig();
    }

    @Override
    public PartitionService getPartitionService() {
        return m_instance.getPartitionService();
    }

    @Override
    public QuorumService getQuorumService() {
        return m_instance.getQuorumService();
    }

    @Override
    public ClientService getClientService() {
        return m_instance.getClientService();
    }

    @Override
    public LoggingService getLoggingService() {
        return m_instance.getLoggingService();
    }

    @Override
    public LifecycleService getLifecycleService() {
        return m_instance.getLifecycleService();
    }

    @Deprecated
    @Override
    public <T extends DistributedObject> T getDistributedObject(String serviceName, Object id) {
        return m_instance.getDistributedObject(serviceName, id);
    }

    @Override
    public <T extends DistributedObject> T getDistributedObject(String serviceName, String name) {
        return m_instance.getDistributedObject(serviceName, getPrefix(name));
    }

    @Override
    public ConcurrentMap<String, Object> getUserContext() {
        return m_instance.getUserContext();
    }

    @Override
    public HazelcastXAResource getXAResource() {
        return m_instance.getXAResource();
    }

    @Override
    public void shutdown() {
        m_instance.shutdown();
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
        for(DistributedObject object : m_instance.getDistributedObjects()) {
            if(type == null) {
                rv.add(object);
            } else if(type.isAssignableFrom(object.getClass())) {
                rv.add(object);
            }
        }

        return  rv;
    }
}

