/*
 * Copyright (c) 2016 Luca Burgazzoli
 *
 * https://github.com/lburgazzoli
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.lburgazzoli.hazelcast.osgi.examples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.util.Enumeration;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.wiring.BundleWiring;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator implements BundleActivator, Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(Activator.class);
    private static final String PROVIDER_CLASS = "com.hazelcast.cache.HazelcastCachingProvider";
    private static final String SERVICE_PATH = "META-INF/services/javax.cache.spi.CachingProvider";

    private final ScheduledExecutorService scheduler;

    private CachingProvider provider;
    private CacheManager manager;
    private Cache<String, Bean> cache;
    private ClassLoader jcl;

    public Activator() {
        this.scheduler = Executors.newScheduledThreadPool(1);
    }

    // ******************
    // BundleActivator
    // ******************

    @Override
    public void start(BundleContext context) throws Exception {
        this.jcl = findClassLoader(context);
        ClassLoader ccl = Thread.currentThread().getContextClassLoader();

        try {
            Thread.currentThread().setContextClassLoader(jcl);

            this.provider = Caching.getCachingProvider(PROVIDER_CLASS, jcl);
            this.manager  = provider.getCacheManager(null, jcl, null);

            this.cache = manager.createCache(
                "my-cache",
                new MutableConfiguration<String, Bean>()
                    .setTypes(String.class, Bean.class)
                    .setManagementEnabled(false)
            );
        } finally {
            Thread.currentThread().setContextClassLoader(ccl);
        }

        this.scheduler.scheduleAtFixedRate(this, 5, 1, TimeUnit.SECONDS);
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        this.manager.destroyCache("my-cache");
    }

    // ******************
    // Runnable
    // ******************

    @Override
    public void run() {
        LOGGER.info("Cache {}", cache);
        if (cache != null) {
            ClassLoader ccl = Thread.currentThread().getContextClassLoader();
            try {
                Thread.currentThread().setContextClassLoader(jcl);
                Bean b = new Bean(UUID.randomUUID().toString());

                LOGGER.info("Put {}", b);
                cache.put("myBean", b);
                LOGGER.info("Put done", b);
                LOGGER.info("Get {}", cache.get("myBean"));
            }finally {
                Thread.currentThread().setContextClassLoader(ccl);
            }
        }
    }

    // ******************
    // Helpers
    // ******************

    private ClassLoader findClassLoader(BundleContext context) throws Exception {
        final ClassLoader bcl = context.getBundle().adapt(BundleWiring.class).getClassLoader();

        for (final Bundle bundle: context.getBundles()) {
            URL spi = bundle.getResource(SERVICE_PATH);
            if (spi != null) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(spi.openStream()))) {
                    if (in.readLine().equals(PROVIDER_CLASS)) {
                        return new ClassLoader(bcl) {
                            @Override
                            protected Class<?> findClass(String name) throws ClassNotFoundException {
                                Class<?> answer = bundle.loadClass(name);
                                LOGGER.info("findClass {} = {}", name, answer);
                                return answer;
                            }
                            @Override
                            protected URL findResource(String name) {
                                URL answer = bundle.getResource(name);
                                LOGGER.info("findResource {} = {}", name, answer);
                                return answer;
                            }
                            @Override
                            protected Enumeration<URL> findResources(String name) throws IOException {
                                Enumeration<URL> answer = bundle.getResources(name);
                                LOGGER.info("findResources {} = {}", name, answer);
                                return answer;
                            }
                        };
                    }
                }
            }
        }

        return bcl;
    }

    // ******************
    //
    // ******************

    public static class Bean implements Serializable {
        private String data;

        public Bean() {
            this.data = null;
        }

        public Bean(String data) {
            this.data = data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public String getData() {
            return data;
        }

        @Override
        public String toString() {
            return "Bean{" +
                "data='" + data + '\'' +
                '}';
        }
    }
}
