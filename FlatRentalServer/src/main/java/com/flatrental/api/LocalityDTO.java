package com.flatrental.api;

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
    @NotNull
    private String name;

    @NotNull
    private LocalityType localityType;

}
