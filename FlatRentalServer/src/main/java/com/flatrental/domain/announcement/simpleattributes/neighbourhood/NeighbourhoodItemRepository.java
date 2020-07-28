package com.flatrental.domain.announcement.simpleattributes.neighbourhood;

import com.flatrental.domain.announcement.simpleattributes.SimpleAttributeCacheableJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NeighbourhoodItemRepository extends SimpleAttributeCacheableJpaRepository<NeighbourhoodItem, Long> {

}
