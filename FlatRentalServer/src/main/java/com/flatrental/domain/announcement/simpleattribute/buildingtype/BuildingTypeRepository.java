package com.flatrental.domain.announcement.simpleattribute.buildingtype;

import com.flatrental.domain.announcement.simpleattribute.SimpleAttributeCacheableJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuildingTypeRepository extends SimpleAttributeCacheableJpaRepository<BuildingType, Long> {

}
