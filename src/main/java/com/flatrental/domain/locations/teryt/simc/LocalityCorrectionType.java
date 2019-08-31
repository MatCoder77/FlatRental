package com.flatrental.domain.locations.teryt.simc;

import java.text.MessageFormat;
import java.util.Arrays;

public enum LocalityCorrectionType {

    ADDITION("D"),
    EDITION("Z"),
    MOVE("P"),
    DELETION("U");

    private String symbol;

    private static final String CANNOT_CONVERT = "Cannot convert from {0} to AdministrationUnitCorrectionType";

    LocalityCorrectionType(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public static LocalityCorrectionType fromString(String symbol) {
        return Arrays.stream(LocalityCorrectionType.values())
                .filter(type -> type.symbol.equals(symbol))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(MessageFormat.format(CANNOT_CONVERT, symbol)));
    }

}
