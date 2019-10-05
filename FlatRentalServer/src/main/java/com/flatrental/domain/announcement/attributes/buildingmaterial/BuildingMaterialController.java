package com.flatrental.domain.announcement.attributes.buildingmaterial;

import com.flatrental.api.SimpleResourceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/buildingmaterial")
public class BuildingMaterialController {

    @Autowired
    private BuildingMaterialService buildingMaterialService;

    @GetMapping
    public List<SimpleResourceDTO> getBuildingMaterials() {
        return buildingMaterialService.getAllBuildingMaterials().stream()
                .map(buildingMaterialService::mapToSimpleResourceDTO)
                .collect(Collectors.toList());
    }

}
