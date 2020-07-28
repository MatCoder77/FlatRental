package com.flatrental.domain.announcement.simpleattributes.furnishings;

import com.flatrental.api.SimpleResourceDTO;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Api(tags = "Reference Attributes")
@RestController
@RequestMapping("/api/furnishing")
@RequiredArgsConstructor
public class FurnishingController {

    private final FurnishingService furnishingService;

    private static final String FURNISHING_TYPE_PARAM = "type";
    private static final boolean TRUE_VALUE_WHEN_NO_TYPE_SUPPLIED = true;

    @GetMapping
    public List<SimpleResourceDTO> getFurnishing(@RequestParam(FURNISHING_TYPE_PARAM) Optional<FurnishingType> furnishingType) {
        return furnishingService.getFurnishingItems().stream()
                .filter(item -> hasSuppliedFurnishingType(item, furnishingType))
                .map(furnishingService::mapToSimpleResourceDTO)
                .collect(Collectors.toList());
    }

    private boolean hasSuppliedFurnishingType(FurnishingItem furnishingItem, Optional<FurnishingType> furnishingType) {
        return furnishingType.map(type -> furnishingItem.getFurnishingType() == type)
                .orElse(TRUE_VALUE_WHEN_NO_TYPE_SUPPLIED);
    }

}
