package com.flatrental.api;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class FileUploadDTO {

    @NotNull
    @NotBlank
    private String fileName;

    @NotNull
    @NotBlank
    private String fileDownloadUri;

    @NotNull
    @NotBlank
    private String fileType;

    private long size;

}
