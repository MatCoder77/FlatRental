package com.flatrental.domain.buildingtype;

import com.flatrental.api.SimpleResourceDTO;
import com.flatrental.infrastructure.security.LoggedUser;
import com.flatrental.infrastructure.security.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/buildingtype")
public class BuildingTypeController {

    @Autowired
    private BuildingTypeService buildingTypeService;

    @GetMapping
    public List<SimpleResourceDTO> getAllBuildingTypes(@LoggedUser UserInfo userInfo) {
        return buildingTypeService.getAllBuildingTypes().stream()
                .map(this::mapToSimpleResourceDTO)
                .collect(Collectors.toList());
    }

    private SimpleResourceDTO mapToSimpleResourceDTO(BuildingType buildingType) {
        return new SimpleResourceDTO(buildingType.getId(), buildingType.getName());
    }

}
