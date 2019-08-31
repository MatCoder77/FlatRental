package com.flatrental.domain.locations.elasticsearch;

import com.flatrental.domain.locations.street.Street;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StreetSearchDTO {

    private Long id;
    private String main_name;
    private String leading_name;
    private String type;

    public static StreetSearchDTO fromStreet(Street street) {
        return new StreetSearchDTO(street.getId(), street.getMainName(), street.getLeadingName().orElse(null), street.getStreetType().getReadableValue());
    }

}
