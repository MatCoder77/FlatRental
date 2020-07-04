package com.flatrental.domain.announcement.simpleattributes.kitchentype;


import com.flatrental.api.SimpleResourceDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/kitchentype")
@RequiredArgsConstructor
public class KitchenTypeController {

    private final KitchenTypeService kitchenTypeService;

    @GetMapping
    public List<SimpleResourceDTO> getKitchenTypes() {
        return kitchenTypeService.getAllKitchenTypes().stream()
                .map(kitchenTypeService::mapToSimpleResourceDTO)
                .collect(Collectors.toList());
    }

}
