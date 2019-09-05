package com.flatrental.domain.buildingmaterial;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuildingMaterialRepository extends JpaRepository<BuildingMaterial, Long> {

}

