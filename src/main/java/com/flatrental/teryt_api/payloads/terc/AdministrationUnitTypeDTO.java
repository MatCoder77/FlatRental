package com.flatrental.teryt_api.payloads.terc;

import java.util.Arrays;
import java.util.Optional;

public enum AdministrationUnitTypeDTO {

    VOIVODESHIP("województwo"),
    DISTRICT("powiat"),
    DISTRICT_CITY("miasto na prawach powiatu"),
    DISTRICT_CAPITAL("miasto stołeczne, na prawach powiatu"),
    URBAN_COMMUNE("gmina miejska"),
    RURAL_COMMUNE("gmina wiejska"),
    MIXED_COMMUNE("gmina miejsko-wiejska"),
    CAPITAL_COMMUNE("gmina miejska, miasto stołeczne"),
    CITY_IN_MIXED_COMMUNE("miasto"),
    RURAL_AREA_IN_MIXED_COMMUNE("obszar wiejski"),
    DISTRICT_OF_CAPITAL("dzielnica"),
    DISTRICT_OF_CITY("delegatura");

    private String typeValue;

    AdministrationUnitTypeDTO(String typeValue) {
        this.typeValue = typeValue;
    }

    public String getTypeValue() {
        return typeValue;
    }

    public static Optional<AdministrationUnitTypeDTO> fromString(String typeValue) {
        return Arrays.stream(AdministrationUnitTypeDTO.values())
                .filter(unitType -> unitType.getTypeValue().equals(typeValue))
                .findAny();
    }

}
