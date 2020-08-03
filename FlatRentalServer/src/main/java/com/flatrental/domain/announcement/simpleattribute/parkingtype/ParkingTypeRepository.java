package com.flatrental.domain.announcement.simpleattribute.parkingtype;

import com.flatrental.domain.announcement.simpleattribute.SimpleAttributeCacheableJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingTypeRepository extends SimpleAttributeCacheableJpaRepository<ParkingType, Long> {

}
