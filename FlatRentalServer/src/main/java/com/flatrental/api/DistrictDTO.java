package com.flatrental.api;

import com.flatrental.domain.locations.district.DistrictType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class DistrictDTO {

    @NonNull
    private Long id;

    @NotBlank
    private String name;

    private DistrictType type;

}
