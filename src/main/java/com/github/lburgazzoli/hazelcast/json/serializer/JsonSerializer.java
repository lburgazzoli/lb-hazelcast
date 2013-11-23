package com.github.lburgazzoli.hazelcast.json.serializer;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.nio.serialization.ByteArraySerializer;

import java.io.IOException;

/**
 *
 */
public class JsonSerializer<T> implements ByteArraySerializer<T> {

    private final ObjectMapper m_mapper;
    private final Class<T> m_type;

    /**
     * c-tor
     *
     * @param type
     */
    public JsonSerializer(Class<T> type) {
        m_type = type;
        m_mapper = new ObjectMapper();
    }

    /**
     * c-tor
     *
     * @param type
     * @param factory
     */
    public JsonSerializer(Class<T> type, JsonFactory factory) {
        m_type = type;
        m_mapper = new ObjectMapper(factory);
    }

    @Override
    public int getTypeId() {
        return 5;
    }

    @Override
    public void destroy() {
    }

    @Override
    public byte[] write(T object) throws IOException {
        return m_mapper.writeValueAsBytes(object);
    }

    @Override
    public T read(byte[] bytes) throws IOException {
        return m_mapper.readValue(bytes, m_type);
    }
}
