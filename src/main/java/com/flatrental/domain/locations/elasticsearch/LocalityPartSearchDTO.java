package com.flatrental.domain.locations.elasticsearch;

import com.flatrental.domain.locations.localitypart.LocalityPart;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LocalityPartSearchDTO {

    private Long id;
    private String name;
    private String type;

    public static LocalityPartSearchDTO fromLocalityPart(LocalityPart localityPart) {
        return new LocalityPartSearchDTO(localityPart.getId(), localityPart.getName(), localityPart.getLocalityType().getTypeName());
    }

}
