package com.flatrental.domain.announcement.simpleattributes.furnishings;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FurnishingRepository extends JpaRepository<FurnishingItem, Long> {

    List<FurnishingItem> getFurnishingItemsByFurnishingType(FurnishingType furnishingType);

}
