package com.flatrental.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class SimpleResourceDTO {

    @NotNull
    private Long id;

    private String value;

    SimpleResourceDTO(Long id) {
        this.id = id;
    }

}
