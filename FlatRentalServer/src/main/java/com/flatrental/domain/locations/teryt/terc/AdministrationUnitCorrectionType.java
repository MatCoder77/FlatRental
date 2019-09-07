package com.flatrental.domain.locations.teryt.terc;

import java.text.MessageFormat;
import java.util.Arrays;

public enum AdministrationUnitCorrectionType {

    ADDITION("D"),
    MODIFICATION("M"),
    DELETION("U");

    private String symbol;

    private static final String CANNOT_CONVERT = "Cannot convert from {0} to AdministrationUnitCorrectionType";

    AdministrationUnitCorrectionType(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public static AdministrationUnitCorrectionType fromString(String symbol) {
        return Arrays.stream(AdministrationUnitCorrectionType.values())
                .filter(correctionType -> correctionType.symbol.equals(symbol))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(MessageFormat.format(CANNOT_CONVERT, symbol)));
    }

}
