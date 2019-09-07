package com.flatrental.domain.locations.teryt.terc;

import java.util.Arrays;
import java.util.Optional;

public enum AdministrationUnitTypeCode {

    URBAN_COMMUNE(1),
    RURAL_COMMUNE(2),
    MIXED_COMMUNE(3),
    CITY_IN_MIXED_COMMUNE(4),
    RURAL_AREA_IN_MIXED_COMMUNE(5),
    DISTRICT_OF_CAPITAL(8),
    DISTRICT_OF_CITY(9);

    private int code;
    AdministrationUnitTypeCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return  code;
    }

    public static Optional<AdministrationUnitTypeCode> fromString(String code) {
        return Arrays.stream(AdministrationUnitTypeCode.values())
                .filter(typeCode -> Integer.valueOf(code).equals(typeCode.getCode()))
                .findAny();
    }

}
