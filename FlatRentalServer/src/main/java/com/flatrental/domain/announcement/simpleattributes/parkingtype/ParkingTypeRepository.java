package com.flatrental.domain.announcement.simpleattributes.parkingtype;

import com.flatrental.domain.announcement.simpleattributes.SimpleAttributeCacheableJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingTypeRepository extends SimpleAttributeCacheableJpaRepository<ParkingType, Long> {

}
