package com.flatrental.domain.announcement.attributes.neighbourhood;

import com.flatrental.api.SimpleResourceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/neighbourhood")
public class NeighborhoodItemController {

    @Autowired
    private NeighbourhoodItemService neighbourhoodItemService;

    @GetMapping
    public List<SimpleResourceDTO> getNeighborhoodItems() {
        return neighbourhoodItemService.getAllNeighbourItems()
                .stream()
                .map(this::mapToSimpleResourceDTO)
                .collect(Collectors.toList());
    }

    private SimpleResourceDTO mapToSimpleResourceDTO(NeighbourhoodItem neighbourhoodItem) {
        return new SimpleResourceDTO(neighbourhoodItem.getId(), neighbourhoodItem.getName());
    }

}
