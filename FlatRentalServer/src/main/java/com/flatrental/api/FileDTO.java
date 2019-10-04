package com.flatrental.api;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class FileDTO {

    @NotNull
    private String filename;

    private String data;

}
