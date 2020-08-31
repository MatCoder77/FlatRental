package com.flatrental.api.location;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class VoivodeshipDTO {

    @NonNull
    private Long id;

    @NotBlank
    private String name;

}
