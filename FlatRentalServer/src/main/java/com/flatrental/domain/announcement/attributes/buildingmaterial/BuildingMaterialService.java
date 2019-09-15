package com.flatrental.domain.announcement.attributes.buildingmaterial;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BuildingMaterialService {

    @Autowired
    private BuildingMaterialRepository buildingMaterialRepository;


    public List<BuildingMaterial> getAllBuildingMaterials() {
        return buildingMaterialRepository.findAll();
    }

}
