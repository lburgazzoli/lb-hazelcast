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

public class HzSerializer<T> implements Serializer {
    protected final Class<T> m_type;
    protected final int m_typeId;

    protected HzSerializer(final Class<T> type, int typeId) {
        m_type   = type;
        m_typeId = typeId;
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
}
