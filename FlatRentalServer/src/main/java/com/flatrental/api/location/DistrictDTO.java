package com.flatrental.api.location;

import com.flatrental.domain.locations.district.DistrictType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class DistrictDTO {

    @NonNull
    private Long id;

    @NotBlank
    private String name;

    private DistrictType type;

}
