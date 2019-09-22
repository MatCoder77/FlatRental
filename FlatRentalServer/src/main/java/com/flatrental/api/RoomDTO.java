package com.flatrental.api;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Data
@AllArgsConstructor
public class RoomDTO {

    @Positive
    private Integer numberOfPersons;

    @PositiveOrZero
    private String personsOccupied;

    @Positive
    private Integer area;

    @Positive
    private Integer pricePerMonth;

    private List<SimpleResourceDTO> furnishing;

    @Positive
    private Integer roomNumber;

}
