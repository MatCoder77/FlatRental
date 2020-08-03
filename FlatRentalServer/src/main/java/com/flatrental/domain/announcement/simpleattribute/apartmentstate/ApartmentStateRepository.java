package com.flatrental.domain.announcement.simpleattribute.apartmentstate;

import com.flatrental.domain.announcement.simpleattribute.SimpleAttributeCacheableJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApartmentStateRepository extends SimpleAttributeCacheableJpaRepository<ApartmentState, Long> {

}
