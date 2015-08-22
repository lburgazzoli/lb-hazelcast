/*
 * Copyright (c) 2015 Luca Burgazzoli
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

package com.github.lburgazzoli.hazelcast.config;

import com.github.lburgazzoli.hazelcast.HzUtil;
import javaslang.Function2;
import org.reflections.ReflectionUtils;
import pl.jsolve.typeconverter.TypeConverter;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public class HzConfig {

    public static final Function2<String, String, String> PROPERTY_TO_METHOD_NAME =
        (String prefix, String input) -> {
            final StringBuilder methodName = new StringBuilder(input);
            for(int i = methodName.length() - 1; i >= 0; --i) {
                if(methodName.charAt(i) == '-') {
                    methodName.deleteCharAt(i);
                    if(i < methodName.length()) {
                        methodName.setCharAt(i, Character.toUpperCase(methodName.charAt(i)));
                    }
                }
            }

            methodName.setCharAt(0, Character.toUpperCase(methodName.charAt(0)));
            return prefix + methodName;
        };

    // *************************************************************************
    //
    // *************************************************************************

    /**
     *
     * @param configSupplier
     * @param root
     * @param processor
     * @param <C>
     */
    public static <C> C forEachElementApply(
        final Supplier<C> configSupplier,
        final Map.Entry<String, Object> root,
        final HzConfigProcessor<C> processor) {

        return forEachElementApply(configSupplier, root.getValue(), processor);
    }

    /**
     *
     * @param configSupplier
     * @param root
     * @param processor
     * @param <C>
     */
    @SuppressWarnings("unchecked")
    public static <C> C forEachElementApply(
        final Supplier<C> configSupplier,
        final Object root,
        final HzConfigProcessor<C> processor) {

        if(null == root) {
            throw new IllegalArgumentException("Root node can't be null");
        }

        if(!(root instanceof Map)) {
            throw new IllegalArgumentException("Root node is not a map (" + root.getClass() + ")");
        }

        return forEachElementApply(configSupplier, (Map<String, Object>) root, processor);
    }

    /**
     *
     * @param configSupplier
     * @param root
     * @param processor
     * @param <C>
     */
    public static <C> C forEachElementApply(
            final Supplier<C> configSupplier,
            final Map<String, Object> root,
            final HzConfigProcessor<C> processor) {

        final C config = configSupplier.get();
        root.entrySet().forEach(
            entry -> {
                if (entry.getValue() != null) {
                    processor.apply(config, entry.getKey(), entry.getValue());
                }
            }
        );

        return config;
    }

    /**
     *
     * @param source
     * @param targetClass
     * @param <S>
     * @param <T>
     * @return
     */
    public static <S, T> T convert(S source, Class<T> targetClass) {
        return TypeConverter.convert(source, targetClass);
    }

    /**
     *
     * @param entry
     * @return
     */
    public static List<Map<String, Object>> valueAsListOfMaps(Map.Entry<String, Object> entry) {
        return (List<Map<String, Object>>)entry.getValue();
    }

    // *************************************************************************
    //
    // *************************************************************************

    /**
     *
     * @param object
     * @param propertyName
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> Optional<Method> findMethodForProperty(T object, String propertyName) {
        final Set<Method> methods = ReflectionUtils.getMethods(
            object.getClass(),
            ReflectionUtils.withModifier(Modifier.PUBLIC),
            ReflectionUtils.withName(PROPERTY_TO_METHOD_NAME.apply("set", propertyName))
        );

        return methods.size() == 1 ? methods.stream().findFirst() : Optional.empty();
    }

    /**
     *
     * @param object
     * @param propertyName
     * @param value
     * @param <T>
     */
    public static <T> T setPropertyValue(T object, String propertyName, Object value) {
        try {
            final Optional<Method> method = findMethodForProperty(object, propertyName);

            // Check if method exists
            if(!method.isPresent()) {
                throw new IllegalArgumentException("No setter for " + propertyName);
            }

            // Check if the number of parameters is exactly one
            if(method.get().getParameterCount() != 1) {
                throw new IllegalArgumentException(
                    "Setter is not properly defined: "
                        + " wrong number of parameters (" + method.get().getParameterCount() + ")");
            }

            method.get().invoke(
                object,
                convert(value, method.get().getParameterTypes()[0])
            );
        } catch(Exception e) {
            HzUtil.rethrowUnchecked(e);
        }

        return object;
    }

    // *************************************************************************
    //
    // *************************************************************************

    public static final class Elements {
        public static final String HAZELCAST            = "hazelcast";
        public static final String IMPORT               = "import";
        public static final String GROUP                = "group";
        public static final String LICENSE_KEY          = "license-key";
        public static final String MANAGEMENT_CENTER    = "management-center";
        public static final String PROPERTIES           = "properties";
        public static final String WAN_REPLICATION      = "wan-replication";
        public static final String NETWORK              = "network";
        public static final String PARTITION_GROUP      = "partition-group";
        public static final String EXECUTOR_SERVICE     = "executor-service";
        public static final String QUEUE                = "queue";
        public static final String MAPS                 = "maps";
        public static final String CACHE                = "cache";
        public static final String MULTIMAP             = "multimap";
        public static final String REPLICATED_MAP       = "replicatedmap";
        public static final String LIST                 = "list";
        public static final String SET                  = "set";
        public static final String TOPIC                = "topic";
        public static final String RELIABLE_TOPIC       = "reliable-topic";
        public static final String JOB_TRACKER          = "jobtracker";
        public static final String SEMAPHORE            = "semaphore";
        public static final String RINGBUFFER           = "ringbuffer";
        public static final String LISTENERS            = "listeners";
        public static final String SERIALIZATION        = "serialization";
        public static final String SERVICES             = "services";
        public static final String SECURITY             = "security";
        public static final String MEMBER_ATTRIBUTES    = "member-attributes";
        public static final String NATIVE_MEMORY        = "native-memory";
        public static final String QUORUM               = "quorum";
    }
}
