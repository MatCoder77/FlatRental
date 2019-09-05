package com.flatrental.api;

import lombok.Data;

@Data
public class TokenDTO {

    private String token;
    private String tokenType = "Barer";

    public TokenDTO(String token) {
        this.token = token;
    }

}
