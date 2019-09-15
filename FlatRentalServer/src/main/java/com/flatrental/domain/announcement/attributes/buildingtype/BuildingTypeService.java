package com.flatrental.domain.announcement.attributes.buildingtype;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BuildingTypeService {

    @Autowired
    private BuildingTypeRepository buildingTypeRepository;

    public List<BuildingType> getAllBuildingTypes() {
        return buildingTypeRepository.findAll();
    }

}
