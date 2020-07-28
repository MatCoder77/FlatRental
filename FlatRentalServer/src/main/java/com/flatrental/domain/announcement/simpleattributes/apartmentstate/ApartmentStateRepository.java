package com.flatrental.domain.announcement.simpleattributes.apartmentstate;

import com.flatrental.domain.announcement.simpleattributes.SimpleAttributeCacheableJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApartmentStateRepository extends SimpleAttributeCacheableJpaRepository<ApartmentState, Long> {

}
