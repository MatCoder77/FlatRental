package com.flatrental.domain.locations.elasticsearch;

public enum Index {

    LOCATIONS("locations");

    private String name;

    Index(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
