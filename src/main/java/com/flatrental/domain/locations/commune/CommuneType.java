package com.flatrental.domain.locations.commune;

import com.flatrental.teryt_api.payloads.terc.AdministrationUnitTypeDTO;

import java.text.MessageFormat;
import java.util.Arrays;

public enum CommuneType {

    URBAN_COMMUNE,
    RURAL_COMMUNE,
    MIXED_COMMUNE,
    CAPITAL_COMMUNE;

    private static final String CANNOT_CONVERT_MSG = "Cannot convert value {0} to DistrictType";

    public static CommuneType fromAdministrationUnitTypeDTO(AdministrationUnitTypeDTO administrationUnitType) {
        return Arrays.stream(CommuneType.values())
                .filter(districtType -> districtType.name().equals(administrationUnitType.name()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(MessageFormat.format(CANNOT_CONVERT_MSG, administrationUnitType.name())));
    }

}
