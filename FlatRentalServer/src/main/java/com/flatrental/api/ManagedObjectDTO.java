package com.flatrental.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ManagedObjectDTO {

    @NotNull
    private Instant createdAt;

    @NotNull
    private Instant updatedAt;

    @NotNull
    private UserDTO createdBy;

    @NotNull
    private UserDTO updatedBy;

    @NotNull
    private String objectState;

}
