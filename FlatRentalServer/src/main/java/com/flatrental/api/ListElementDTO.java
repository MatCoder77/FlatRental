package com.flatrental.api;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ListElementDTO {

    private String value;
    private String readableValue;

}
