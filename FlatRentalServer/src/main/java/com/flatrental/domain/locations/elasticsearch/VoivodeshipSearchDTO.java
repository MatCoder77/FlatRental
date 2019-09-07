package com.flatrental.domain.locations.elasticsearch;

import com.flatrental.domain.locations.voivodeship.Voivodeship;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VoivodeshipSearchDTO {

    private Long id;
    private String name;

    public static VoivodeshipSearchDTO fromVoivodeship(Voivodeship voivodeship) {
        return new VoivodeshipSearchDTO(voivodeship.getId(), voivodeship.getName());
    }

}
