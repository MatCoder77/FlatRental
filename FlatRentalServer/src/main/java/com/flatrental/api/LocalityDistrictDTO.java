package com.flatrental.api;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class LocalityDistrictDTO {

    @NotNull
    private Long id;

    @NotBlank
    private String name;

}
