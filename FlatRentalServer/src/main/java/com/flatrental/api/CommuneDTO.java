package com.flatrental.api;

import com.flatrental.domain.locations.commune.CommuneType;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class CommuneDTO {

    @NotNull
    private Long id;

    @NotBlank
    private String name;

    private CommuneType type;

}
