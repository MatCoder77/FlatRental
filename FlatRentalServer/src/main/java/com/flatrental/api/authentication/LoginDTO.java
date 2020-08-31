package com.flatrental.api.authentication;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class LoginDTO {

    @NotNull
    @NotBlank
    private String usernameOrEmail;

    @NotNull
    @NotBlank
    private String password;

}
