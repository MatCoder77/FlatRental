package com.flatrental.domain.locations.elasticsearch;

import com.flatrental.domain.locations.locality.Locality;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LocalitySearchDTO {

    private Long id;
    private String name;
    private String type;

    public static LocalitySearchDTO fromLocality(Locality locality) {
        return new LocalitySearchDTO(locality.getId(), locality.getName(), locality.getLocalityType().getTypeName());
    }

}
