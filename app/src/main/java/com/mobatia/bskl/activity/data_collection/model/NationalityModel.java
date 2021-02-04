package com.mobatia.bskl.activity.data_collection.model;

import java.io.Serializable;

public class NationalityModel implements Serializable {

    String id;
    String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
