package com.flatrental.api.simpleattribute;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class SimpleAttributeDTO {

    @NotNull
    private Long id;

    private String value;

}
