package com.flatrental.domain.announcement.simpleattributes.buildingtype;

import com.flatrental.api.SimpleResourceDTO;
import com.flatrental.infrastructure.security.LoggedUser;
import com.flatrental.infrastructure.security.UserInfo;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "Reference Attributes")
@RestController
@RequestMapping("/api/buildingtype")
@RequiredArgsConstructor
public class BuildingTypeController {

    private final BuildingTypeService buildingTypeService;

    @GetMapping
    public List<SimpleResourceDTO> getAllBuildingTypes(@LoggedUser UserInfo userInfo) {
        return buildingTypeService.getAllBuildingTypes().stream()
                .map(buildingTypeService::mapToSimpleResourceDTO)
                .collect(Collectors.toList());
    }

}
