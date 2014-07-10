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

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

public class HzConf extends Config implements Cloneable {
    private static final Logger LOGGER = LoggerFactory.getLogger(HzConf.class);

    private final String m_prefix;

    public HzConf() {
        this(null);
    } 	
	
    public HzConf(final String prefix) {
        super();
        super.setProperty("hazelcast.logging.type","slf4j");

        m_prefix = prefix;
    }

    // *************************************************************************
    // Multicast
    // *************************************************************************

    public MulticastConfig getMulticastConfig() {
        return getNetworkConfig().getJoin().getMulticastConfig();
    }

    public void setMulticastEnabled(boolean enabled) {
        getMulticastConfig().setEnabled(enabled);
    }

    public void setMulticastGroup(String multicastGroup) {
        getMulticastConfig().setMulticastGroup(multicastGroup);
    }

    public void setMulticastPort(int multicastPort) {
        getMulticastConfig().setMulticastPort(multicastPort);
    }

    public void setMulticastTrustedInterfaces(Set<String> interfaces) {
        getMulticastConfig().setTrustedInterfaces(interfaces);
    }

    // *************************************************************************
    // TCP/IP
    // *************************************************************************

    public TcpIpConfig getTcpIpConfig() {
        return getNetworkConfig().getJoin().getTcpIpConfig();
    }

    public void setTcpIpEnabled(boolean enabled) {
        getTcpIpConfig().setEnabled(enabled);
    }

    public void setTcpIpConnectionTimeout(int connectionTimeout) {
        getTcpIpConfig().setConnectionTimeoutSeconds(connectionTimeout);
    }

    public void setTcpIpMembers(List<String> members) {
        getTcpIpConfig().setMembers(members);
    }

    public void setTcpIpRequiredMember(String requiredMember) {
        getTcpIpConfig().setRequiredMember(requiredMember);
    }

    // *************************************************************************
    // AWS
    // *************************************************************************

    public AwsConfig getAwsConfig() {
        return getNetworkConfig().getJoin().getAwsConfig();
    }

    public void setAwsEnabled(boolean enabled) {
        getAwsConfig().setEnabled(enabled);
    }

    // *************************************************************************
    // Interface
    // *************************************************************************

    public InterfacesConfig getInterfacesConfig() {
        return getNetworkConfig().getInterfaces();
    }

    public void setInterfacesEnabled(boolean enabled) {
        getInterfacesConfig().setEnabled(enabled);
    }

    public void setInterfaces(List<String> interfaces) {
        getInterfacesConfig().setInterfaces(interfaces);
    }

    // *************************************************************************
    // Instances
    // *************************************************************************

    public HazelcastInstance newHazelcastInstance() {
        try {
            final HazelcastInstance hz = Hazelcast.newHazelcastInstance((Config)this.clone());
            return m_prefix == null ? hz : new HzPrefixedInstance(hz,m_prefix);
        } catch (CloneNotSupportedException e) {
            LOGGER.warn("CloneNotSupportedException",e);
        }

        return null;
    }
}
