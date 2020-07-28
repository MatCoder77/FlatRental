package com.flatrental.domain.announcement.simpleattributes.heatingtype;

import com.flatrental.domain.announcement.simpleattributes.SimpleAttributeCacheableJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeatingTypeRepository extends SimpleAttributeCacheableJpaRepository<HeatingType, Long> {

}
