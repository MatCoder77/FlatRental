package com.flatrental.api.location;

import com.flatrental.domain.locations.teryt.ulic.StreetType;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class StreetDTO {

    @NotNull
    private Long id;

    @NotBlank
    private String mainName;

    private String leadingName;

    @NotBlank
    private StreetType type;

}
