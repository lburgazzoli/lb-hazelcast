package com.github.lburgazzoli.hazelcast.serialization.examples.kryo;

import java.io.Serializable;

public final class KryoCustomer implements Serializable {
    private int id;
    private String name;

    public KryoCustomer() {
        this(-1, "");
    }

    public KryoCustomer(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Customer {"
            + "id=" + id + ","
            + "name=" + name
            + "}";
    }

    public static KryoCustomer newCustomer(int id, String name) {
        final KryoCustomer c = new KryoCustomer();
        c.setId(id);
        c.setName(name);

        return c;
    }
}
