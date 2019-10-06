package com.flatrental.domain.announcement.simpleattributes.buildingmaterial;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuildingMaterialRepository extends JpaRepository<BuildingMaterial, Long> {

}

