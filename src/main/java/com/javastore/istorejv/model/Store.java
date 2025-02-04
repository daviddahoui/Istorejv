package com.javastore.istorejv.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Store {
    private int id;
    private String name;

    public Store(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Store{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

