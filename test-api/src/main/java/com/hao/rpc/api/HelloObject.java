package com.hao.rpc.api;

import lombok.Data;

import java.io.Serializable;

@Data
public class HelloObject implements Serializable {
    private String name;
    private int age;

    public HelloObject() {
    }

    public HelloObject(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
