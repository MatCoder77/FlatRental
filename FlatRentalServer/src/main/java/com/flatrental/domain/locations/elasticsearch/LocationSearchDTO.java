package com.flatrental.domain.locations.elasticsearch;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LocationSearchDTO {

    private VoivodeshipSearchDTO voivodeship;
    private DistrictSearchDTO district;
    private CommuneSearchDTO commune;
    private LocalitySearchDTO locality;
    private LocalityDistrictSearchDTO localityDistrict;
    private LocalityPartSearchDTO localityPart;
    private StreetSearchDTO street;

}
