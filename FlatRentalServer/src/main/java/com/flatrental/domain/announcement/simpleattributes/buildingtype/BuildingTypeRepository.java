package com.flatrental.domain.announcement.simpleattributes.buildingtype;

import com.flatrental.domain.announcement.simpleattributes.SimpleAttributeCacheableJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuildingTypeRepository extends SimpleAttributeCacheableJpaRepository<BuildingType, Long> {

}
