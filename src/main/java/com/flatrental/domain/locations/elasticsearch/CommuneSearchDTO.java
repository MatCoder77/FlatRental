package com.flatrental.domain.locations.elasticsearch;

import com.flatrental.domain.locations.commune.Commune;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommuneSearchDTO {

    private Long id;
    private String name;
    private String type;

    public static CommuneSearchDTO formCommune(Commune commune) {
        return new CommuneSearchDTO(commune.getId(), commune.getName(), commune.getType().toString());
    }

}
