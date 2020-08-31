package com.flatrental.domain.simpleattribute.parkingtype;

import com.flatrental.domain.simpleattribute.SimpleAttributeCacheableJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingTypeRepository extends SimpleAttributeCacheableJpaRepository<ParkingType, Long> {

}
