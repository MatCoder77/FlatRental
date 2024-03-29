package com.flatrental.api;

import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.NaturalId;

import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.net.URI;
import java.util.Set;

@Data
@Builder
public class UserDTO {

    @Id
    private Long id;

    @NotBlank
    @Size(max = 40)
    private String name;

    @NotBlank
    @Size(max = 40)
    private String surname;

    @NotBlank
    @Size(max = 15)
    private String username;

    @NotBlank
    @Size(max = 100)
    private String password;

    @NaturalId
    @NotBlank
    @Size(max = 40)
    @Email
    private String email;

    @NotBlank
    private String phoneNumber;

    private URI avatarUrl;

    private Set<String> roles;

    private UserStatisticsDTO statistics;

    private String about;

}
