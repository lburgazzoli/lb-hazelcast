package com.github.lburgazzoli.hazelcast.offheap.hft;

/**
 * @author lburgazzoli
 */
public interface OffHeapDataVal {
    public byte[] getAtomicData(byte[] data);
    public void addAtomicData(byte[] data);
}
