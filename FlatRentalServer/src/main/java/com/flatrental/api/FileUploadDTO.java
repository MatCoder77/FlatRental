package com.flatrental.api;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.net.URI;

@Data
@AllArgsConstructor
public class FileUploadDTO {

    @NotNull
    @NotBlank
    private String fileName;

    @NotNull
    private URI fileDownloadUri;

    @NotNull
    @NotBlank
    private String fileType;

    private long size;

}
