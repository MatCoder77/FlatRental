package com.flatrental.domain.simpleattribute.neighbourhood;

import com.flatrental.domain.simpleattribute.SimpleAttributeCacheableJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NeighbourhoodItemRepository extends SimpleAttributeCacheableJpaRepository<NeighbourhoodItem, Long> {

}
