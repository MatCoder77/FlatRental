package com.flatrental.domain.locations.elasticsearch;

public enum FieldType {

    TEXT("text"),
    KEYWORD("keyword"),
    DATE("date"),
    LONG("long"),
    DOUBLE("long"),
    BOOLEAN("boolean");

    String type;

    FieldType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
