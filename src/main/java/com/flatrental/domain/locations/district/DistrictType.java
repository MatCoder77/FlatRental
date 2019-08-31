package com.flatrental.domain.locations.district;


import com.flatrental.domain.locations.teryt.terc.AdministrationUnitTypeDTO;

import java.text.MessageFormat;
import java.util.Arrays;

public enum DistrictType {

    DISTRICT,
    DISTRICT_CITY,
    DISTRICT_CAPITAL;

    private static final String CANNOT_CONVERT_MSG = "Cannot convert value {0} to DistrictType";

    public static DistrictType fromAdministrationUnitTypeDTO(AdministrationUnitTypeDTO type) {
        return Arrays.stream(DistrictType.values())
                .filter(districtType -> districtType.name().equals(type.name()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(MessageFormat.format(CANNOT_CONVERT_MSG, type.name())));
    }

}
