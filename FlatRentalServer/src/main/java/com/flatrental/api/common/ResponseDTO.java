package com.flatrental.api.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseDTO {

    private boolean success;
    private String message;

}
