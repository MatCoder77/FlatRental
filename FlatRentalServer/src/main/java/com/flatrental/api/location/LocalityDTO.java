package com.flatrental.api.location;

import com.flatrental.domain.locations.localitytype.LocalityType;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class LocalityDTO {

    @NotNull
    private Long id;

    @NotBlank
    private String name;

    private LocalityType type;

}
