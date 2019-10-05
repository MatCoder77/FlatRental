package com.flatrental.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class RoomDTO {

    private Long id;

    @Positive
    private Integer numberOfPersons;

    @PositiveOrZero
    private Integer personsOccupied;

    @Positive
    private Integer area;

    @Positive
    private Integer pricePerMonth;

    private List<SimpleResourceDTO> furnishing;

    @Positive
    @NotNull
    private Integer roomNumber;

}
