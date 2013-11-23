package com.github.lburgazzoli.hazelcast.json.serializer;

import com.fasterxml.jackson.dataformat.smile.SmileFactory;
 
/**
 *
 */
public class BinaryJsonSerializer<T> extends JsonSerializer<T> {

    /**
     * c-tor
     */
    public BinaryJsonSerializer(Class<T> type) {
        super(type, new SmileFactory());
    }

    @Override
    public int getTypeId() {
        return 5;
    }
}
