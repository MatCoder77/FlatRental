package com.flatrental.domain.announcement.simpleattributes.buildingmaterial;

import com.flatrental.api.SimpleResourceDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/buildingmaterial")
@RequiredArgsConstructor
public class BuildingMaterialController {

    private final BuildingMaterialService buildingMaterialService;

    @GetMapping
    public List<SimpleResourceDTO> getBuildingMaterials() {
        return buildingMaterialService.getAllBuildingMaterials().stream()
                .map(buildingMaterialService::mapToSimpleResourceDTO)
                .collect(Collectors.toList());
    }

}
