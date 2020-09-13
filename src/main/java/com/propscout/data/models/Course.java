package com.propscout.data.models;

import java.io.Serializable;

public class Course implements Serializable {

    private int id;
    private String alias;
    private String name;

    public Course() {
    }

    public Course(int id, String alias, String name) {
        this.id = id;
        this.alias = alias;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
