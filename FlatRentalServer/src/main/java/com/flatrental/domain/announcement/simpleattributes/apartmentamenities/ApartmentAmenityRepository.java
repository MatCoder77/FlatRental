package com.flatrental.domain.announcement.simpleattributes.apartmentamenities;

import com.flatrental.domain.announcement.simpleattributes.SimpleAttributeCacheableJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApartmentAmenityRepository extends SimpleAttributeCacheableJpaRepository<ApartmentAmenity, Long> {

}
