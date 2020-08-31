package com.flatrental.domain.simpleattribute.buildingtype;

import com.flatrental.domain.simpleattribute.SimpleAttributeCacheableJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuildingTypeRepository extends SimpleAttributeCacheableJpaRepository<BuildingType, Long> {

}
