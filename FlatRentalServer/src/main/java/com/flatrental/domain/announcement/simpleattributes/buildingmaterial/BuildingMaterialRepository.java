package com.flatrental.domain.announcement.simpleattributes.buildingmaterial;

import com.flatrental.domain.announcement.simpleattributes.SimpleAttributeCacheableJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuildingMaterialRepository extends SimpleAttributeCacheableJpaRepository<BuildingMaterial, Long> {

}

