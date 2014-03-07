/*
 * Copyright 2014 lb
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
package com.github.lburgazzoli.hazelcast.offheap.hft;

import net.openhft.lang.io.Bytes;
import net.openhft.lang.model.Byteable;

/**
 * @author lburgazzoli
 *
 * TODO: record lock
 */
public class OffHeapDataVal implements Byteable {
    private Bytes m_bytes;
    private long m_offset;

    public OffHeapDataVal() {
        m_bytes = null;
        m_offset = 0;
    }

    public void set(byte[] bytes) {
        m_bytes.position(m_offset);
        m_bytes.writeInt(bytes.length);
        m_bytes.write(bytes);
    }

    public byte[] get() {
        m_bytes.position(m_offset);

        int len = m_bytes.readInt();
        byte[] buffer = new byte[len];
        m_bytes.readFully(buffer);

        return buffer;
    }

    @Override
    public void bytes(Bytes bytes, long offset) {
        m_bytes  = bytes;
        m_offset = offset;
    }

    @Override
    public Bytes bytes() {
        return m_bytes;
    }

    @Override
    public int maxSize() {
        return (int)m_offset;
    }
}
