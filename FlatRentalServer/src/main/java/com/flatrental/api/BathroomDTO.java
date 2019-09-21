package com.flatrental.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class BathroomDTO {

    private Integer numberOfBathrooms;

    private Boolean separateWC;

    private List<SimpleResourceDTO> furnishing;

}
