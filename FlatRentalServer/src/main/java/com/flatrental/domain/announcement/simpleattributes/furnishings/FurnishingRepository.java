package com.flatrental.domain.announcement.simpleattributes.furnishings;

import com.flatrental.domain.announcement.simpleattributes.SimpleAttributeCacheableJpaRepository;
import com.flatrental.infrastructure.utils.StringConstants;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.util.List;

@Repository
public interface FurnishingRepository extends SimpleAttributeCacheableJpaRepository<FurnishingItem, Long> {

    @QueryHints({
            @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = StringConstants.TRUE),
            @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHE_REGION, value = SIMPLE_ATTR_QUERY_CACHE)
    })
    List<FurnishingItem> getFurnishingItemsByFurnishingType(FurnishingType furnishingType);
}
