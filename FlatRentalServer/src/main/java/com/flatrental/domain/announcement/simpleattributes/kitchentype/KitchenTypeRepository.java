package com.flatrental.domain.announcement.simpleattributes.kitchentype;

import com.flatrental.domain.announcement.simpleattributes.SimpleAttributeCacheableJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KitchenTypeRepository extends SimpleAttributeCacheableJpaRepository<KitchenType, Long> {

}
