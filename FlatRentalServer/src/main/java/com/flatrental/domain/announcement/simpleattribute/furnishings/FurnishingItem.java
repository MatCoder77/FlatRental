package com.flatrental.domain.announcement.simpleattribute.furnishings;

import com.flatrental.domain.announcement.simpleattribute.SimpleAttribute;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "FurnishingItems")
@Immutable
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "furnishingItemCache")
@Getter
@NoArgsConstructor
public class FurnishingItem extends SimpleAttribute {

    @Column(name = "furnishing_type")
    @Enumerated(EnumType.STRING)
    private FurnishingType furnishingType;

    public FurnishingItem(Long id) {
        super(id);
    }

}
