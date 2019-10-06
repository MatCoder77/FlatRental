package com.flatrental.domain.announcement.simpleattributes.buildingmaterial;

import com.flatrental.api.SimpleResourceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
public class BuildingMaterialService {

    @Autowired
    private BuildingMaterialRepository buildingMaterialRepository;

    private static final String NOT_FOUND = "There is no BuildingMaterial with id {0}";

    public List<BuildingMaterial> getAllBuildingMaterials() {
        return buildingMaterialRepository.findAll();
    }

    public SimpleResourceDTO mapToSimpleResourceDTO(BuildingMaterial buildingMaterial) {
        return new SimpleResourceDTO(buildingMaterial.getId(), buildingMaterial.getName());
    }

    public BuildingMaterial getExistingBuildingMaterial(Long id) {
        return buildingMaterialRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(MessageFormat.format(NOT_FOUND, id)));
    }

}
