package com.flatrental.api;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;

@Data
public class VoivodeshipDTO {

    @NonNull
    private Long id;

    @NotBlank
    @NonNull
    private String name;

}
