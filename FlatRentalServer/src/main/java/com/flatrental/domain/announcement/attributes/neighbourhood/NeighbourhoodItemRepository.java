package com.flatrental.domain.announcement.attributes.neighbourhood;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NeighbourhoodItemRepository extends JpaRepository<NeighbourhoodItem, Long> {
}
