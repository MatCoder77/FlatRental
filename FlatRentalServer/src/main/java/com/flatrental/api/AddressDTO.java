package com.flatrental.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AddressDTO {

    private VoivodeshipDTO voivodeship;
    private DistrictDTO district;
    private CommuneDTO commune;
    private LocalityDTO locality;
    private LocalityDistrictDTO localityDistrict;
    private LocalityPartDTO localityPart;
    private StreetDTO street;

}
