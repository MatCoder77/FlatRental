package com.flatrental.domain.simpleattribute.apartmentstate;

import com.flatrental.domain.simpleattribute.SimpleAttributeCacheableJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApartmentStateRepository extends SimpleAttributeCacheableJpaRepository<ApartmentState, Long> {

}
