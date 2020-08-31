package com.flatrental.api.announcement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Positive;

@Data
@Builder
@AllArgsConstructor
public class RoomBrowseDTO {

    private Long id;

    @Positive
    private Integer numberOfPersons;

    @Positive
    private Integer area;

}
