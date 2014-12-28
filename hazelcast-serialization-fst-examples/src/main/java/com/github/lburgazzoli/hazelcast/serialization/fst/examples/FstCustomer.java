package com.github.lburgazzoli.hazelcast.serialization.fst.examples;

import java.io.Serializable;

public final class FstCustomer implements Serializable {
    private int id;
    private String name;

    public FstCustomer() {
        this(-1, "");
    }

    public FstCustomer(int id, String name) {
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

    public static FstCustomer newCustomer(int id, String name) {
        final FstCustomer c = new FstCustomer();
        c.setId(id);
        c.setName(name);

        return c;
    }
}
