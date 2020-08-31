package com.flatrental.domain.locations.teryt.ulic;


import java.text.MessageFormat;
import java.util.Arrays;

public enum StreetType {

    STREET("ul."),
    AVENUE("al."),
    SQUARE("pl."),
    GARDEN_SQUARE("skwer"),
    BOULEVARD("bulw."),
    ROUNDABOUT("rondo"),
    PARK("park"),
    MARKET_SQUARE("rynek"),
    CHAUSSEE("szosa"),
    ROUTE("droga"),
    ESTATE("os."),
    GARDEN("ogród"),
    ISLAND("wyspa"),
    COAST("wyb."),
    OTHER("inne");


    private final String readableValue;

    private static final String CANNOT_CONVERT = "Cannot convert from {0} to StreetType";

    StreetType(String readableValue) {
        this.readableValue = readableValue;
    }

    public String getReadableValue() {
        return readableValue;
    }

    public static StreetType fromString(String type) {
        return Arrays.stream(StreetType.values())
                .filter(streetType -> streetType.readableValue.equals(type))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(MessageFormat.format(CANNOT_CONVERT, type)));
    }

}
