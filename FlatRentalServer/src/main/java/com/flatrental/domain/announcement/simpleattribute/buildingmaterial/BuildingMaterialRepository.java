package com.flatrental.domain.announcement.simpleattribute.buildingmaterial;

import com.flatrental.domain.announcement.simpleattribute.SimpleAttributeCacheableJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuildingMaterialRepository extends SimpleAttributeCacheableJpaRepository<BuildingMaterial, Long> {

}

