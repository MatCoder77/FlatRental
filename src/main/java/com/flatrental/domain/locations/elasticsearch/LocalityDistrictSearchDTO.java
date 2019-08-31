package com.flatrental.domain.locations.elasticsearch;

import com.flatrental.domain.locations.localitydistrict.LocalityDistrict;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LocalityDistrictSearchDTO {

    private Long id;
    private String name;
    private String type;

    public static LocalityDistrictSearchDTO fromLocalityDistrict(LocalityDistrict localityDistrict) {
        return new LocalityDistrictSearchDTO(localityDistrict.getId(), localityDistrict.getName(), localityDistrict.getLocalityType().getTypeName());
    }

}
