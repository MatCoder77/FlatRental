package com.flatrental.domain.simpleattribute.buildingtype;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BuildingTypeService {

    private final BuildingTypeRepository buildingTypeRepository;

    private static final String NOT_FOUND = "There is no BuildingType with id {0}";

    public List<BuildingType> getAllBuildingTypes() {
        return buildingTypeRepository.findAll();
    }

    public BuildingType getExistingBuildingType(Long id) {
        return buildingTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(MessageFormat.format(NOT_FOUND, id)));
    }

}
