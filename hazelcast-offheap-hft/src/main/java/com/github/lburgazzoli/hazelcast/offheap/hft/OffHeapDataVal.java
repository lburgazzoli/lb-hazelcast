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
        m_bytes.writeInt(m_offset,bytes.length);
    }

    public byte[] get() {
        int len = m_bytes.readInt(m_offset);

        byte[] buffer = new byte[0];
        //m_bytes.readFully(buffer,(int)m_offset + 4,len);

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
