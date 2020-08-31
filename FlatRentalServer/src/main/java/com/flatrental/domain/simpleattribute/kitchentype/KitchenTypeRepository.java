package com.flatrental.domain.simpleattribute.kitchentype;

import com.flatrental.domain.simpleattribute.SimpleAttributeCacheableJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KitchenTypeRepository extends SimpleAttributeCacheableJpaRepository<KitchenType, Long> {

}
