package com.flatrental.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
public class AddressDTO {

    @NotNull
    private VoivodeshipDTO voivodeship;

    @NotNull
    private DistrictDTO district;

    @NotNull
    private CommuneDTO commune;

    @NotNull
    private LocalityDTO locality;

    private LocalityDistrictDTO localityDistrict;
    private LocalityPartDTO localityPart;
    private StreetDTO street;



}
