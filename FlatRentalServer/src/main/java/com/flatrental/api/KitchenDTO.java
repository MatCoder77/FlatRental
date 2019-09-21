package com.flatrental.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class KitchenDTO {

    private SimpleResourceDTO kitchenType;
    private Integer kitchenArea;
    private SimpleResourceDTO cookerType;
    private List<SimpleResourceDTO> furnishing;

}
