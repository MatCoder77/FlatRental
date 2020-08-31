package com.flatrental.domain.locations.teryt.ulic;

import java.text.MessageFormat;
import java.util.Arrays;

public enum StreetCorrectionType {

    ADDITION("D"),
    MODIFICATION("M"),
    NAME_MODIFICATION("Z"),
    DELETION("U");

    private final String symbol;

    private static final String CANNOT_CONVERT = "Cannot convert from {0} to StreetCorrectionType";

    StreetCorrectionType(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public static StreetCorrectionType fromString(String symbol) {
        return Arrays.stream(StreetCorrectionType.values())
                .filter(type -> type.symbol.equals(symbol))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(MessageFormat.format(CANNOT_CONVERT, symbol)));
    }

}
