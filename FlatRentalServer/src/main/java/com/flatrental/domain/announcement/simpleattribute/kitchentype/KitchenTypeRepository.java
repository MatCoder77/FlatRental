package com.flatrental.domain.announcement.simpleattribute.kitchentype;

import com.flatrental.domain.announcement.simpleattribute.SimpleAttributeCacheableJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KitchenTypeRepository extends SimpleAttributeCacheableJpaRepository<KitchenType, Long> {

}
