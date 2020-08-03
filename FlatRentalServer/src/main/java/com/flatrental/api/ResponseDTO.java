package com.flatrental.api;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseDTO {

    private boolean success;
    private String message;

}
