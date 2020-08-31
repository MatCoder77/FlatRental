package com.flatrental.domain.simpleattribute.buildingmaterial;

import com.flatrental.domain.simpleattribute.SimpleAttributeCacheableJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuildingMaterialRepository extends SimpleAttributeCacheableJpaRepository<BuildingMaterial, Long> {

}

