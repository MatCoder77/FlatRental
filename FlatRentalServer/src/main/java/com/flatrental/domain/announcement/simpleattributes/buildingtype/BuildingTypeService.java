package com.flatrental.domain.announcement.simpleattributes.buildingtype;

import com.flatrental.api.SimpleResourceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
public class BuildingTypeService {

    @Autowired
    private BuildingTypeRepository buildingTypeRepository;

    private static final String NOT_FOUND = "There is no BuildingType with id {0}";

    public List<BuildingType> getAllBuildingTypes() {
        return buildingTypeRepository.findAll();
    }

    public SimpleResourceDTO mapToSimpleResourceDTO(BuildingType buildingType) {
        return new SimpleResourceDTO(buildingType.getId(), buildingType.getName());
    }

    public BuildingType getExsistingBuildingType(Long id) {
        return buildingTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(MessageFormat.format(NOT_FOUND, id)));
    }

}
