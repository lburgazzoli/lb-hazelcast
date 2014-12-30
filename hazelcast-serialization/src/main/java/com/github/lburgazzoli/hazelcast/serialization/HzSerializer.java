/*
 * Copyright 2014 Luca Burgazzoli
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
package com.github.lburgazzoli.hazelcast.serialization;

import com.hazelcast.nio.serialization.Serializer;

import java.util.function.Supplier;

public class HzSerializer<T,S> implements Serializer, Supplier<S> {
    protected final Class<T> m_type;
    protected final int m_typeId;
    protected final ThreadLocal<S> m_threadLocal;

    protected HzSerializer(final Class<T> type, int typeId) {
        this(type, typeId, () -> null );
    }

    protected HzSerializer(final Class<T> type, int typeId, Supplier<? extends S> supplier) {
        m_type        = type;
        m_typeId      = typeId;
        m_threadLocal = ThreadLocal.withInitial(supplier);
    }

    @Override
    public int getTypeId() {
        return m_typeId;
    }

    @Override
    public void destroy() {
    }

    public Class<T> getType() {
        return m_type;
    }

    public S get() {
        return m_threadLocal.get();
    }
}
