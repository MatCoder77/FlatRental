package com.flatrental.domain.announcement.simpleattribute.neighbourhood;

import com.flatrental.domain.announcement.simpleattribute.SimpleAttribute;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "NeighbourhoodItems")
@Immutable
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "neighbourhoodItemCache")
@NoArgsConstructor
public class NeighbourhoodItem extends SimpleAttribute {

    public NeighbourhoodItem(Long id) {
        super(id);
    }

}
