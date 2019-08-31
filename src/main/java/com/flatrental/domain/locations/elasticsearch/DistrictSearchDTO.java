package com.flatrental.domain.locations.elasticsearch;

import com.flatrental.domain.locations.district.District;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DistrictSearchDTO {

    private Long id;
    private String name;
    private String type;

    public static DistrictSearchDTO fromDistrict(District district) {
        return new DistrictSearchDTO(district.getId(), district.getName(), district.getType().toString());
    }

}
