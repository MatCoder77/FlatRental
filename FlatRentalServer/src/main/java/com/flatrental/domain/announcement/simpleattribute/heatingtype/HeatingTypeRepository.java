package com.flatrental.domain.announcement.simpleattribute.heatingtype;

import com.flatrental.domain.announcement.simpleattribute.SimpleAttributeCacheableJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeatingTypeRepository extends SimpleAttributeCacheableJpaRepository<HeatingType, Long> {

}
