package com.flatrental.api;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.net.URI;

@Data
@Builder
public class UserDTO {

    @NotNull
    private Long id;

    @NotBlank
    @Size(max = 40)
    private String name;

    @NotBlank
    @Size(max = 40)
    private String surname;

    @NotBlank
    private String phoneNumber;

    private URI avatarUrl;
    private String about;
    private UserStatisticsDTO statistics;

}
