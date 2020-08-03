package com.flatrental.domain.announcement.simpleattribute.neighbourhood;

import com.flatrental.domain.announcement.simpleattribute.SimpleAttributeCacheableJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NeighbourhoodItemRepository extends SimpleAttributeCacheableJpaRepository<NeighbourhoodItem, Long> {

}
