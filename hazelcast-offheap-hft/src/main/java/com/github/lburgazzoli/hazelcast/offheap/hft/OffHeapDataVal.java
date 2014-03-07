package com.github.lburgazzoli.hazelcast.offheap.hft;

import net.openhft.lang.io.Bytes;
import net.openhft.lang.model.Byteable;

/**
 * @author lburgazzoli
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
