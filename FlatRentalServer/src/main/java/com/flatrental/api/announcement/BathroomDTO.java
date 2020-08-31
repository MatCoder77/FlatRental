package com.flatrental.api.announcement;

import com.flatrental.api.simpleattribute.SimpleAttributeDTO;
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

    private List<SimpleAttributeDTO> furnishing;

}
