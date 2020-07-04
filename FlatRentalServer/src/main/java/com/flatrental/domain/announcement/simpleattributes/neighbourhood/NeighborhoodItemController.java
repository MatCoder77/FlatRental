package com.flatrental.domain.announcement.simpleattributes.neighbourhood;

import com.flatrental.api.SimpleResourceDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/neighbourhood")
@RequiredArgsConstructor
public class NeighborhoodItemController {

    private final NeighbourhoodItemService neighbourhoodItemService;

    @GetMapping
    public List<SimpleResourceDTO> getNeighborhoodItems() {
        return neighbourhoodItemService.getAllNeighbourItems()
                .stream()
                .map(neighbourhoodItemService::mapToSimpleResourceDTO)
                .collect(Collectors.toList());
    }

}
