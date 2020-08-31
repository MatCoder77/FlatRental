package com.flatrental.api.announcement;

import com.flatrental.api.simpleattribute.SimpleAttributeDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class KitchenDTO {

    private SimpleAttributeDTO kitchenType;
    private Integer kitchenArea;
    private SimpleAttributeDTO cookerType;
    private List<SimpleAttributeDTO> furnishing;

}
