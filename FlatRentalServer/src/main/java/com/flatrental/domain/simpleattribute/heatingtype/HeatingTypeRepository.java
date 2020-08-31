package com.flatrental.domain.simpleattribute.heatingtype;

import com.flatrental.domain.simpleattribute.SimpleAttributeCacheableJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeatingTypeRepository extends SimpleAttributeCacheableJpaRepository<HeatingType, Long> {

}
