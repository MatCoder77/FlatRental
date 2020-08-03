package com.flatrental.domain.announcement.simpleattribute.apartmentamenity;

import com.flatrental.domain.announcement.simpleattribute.SimpleAttribute;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "ApartmentAmenities")
@Immutable
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "apartmentAmenityCache")
@NoArgsConstructor
public class ApartmentAmenity extends SimpleAttribute {

    public ApartmentAmenity(Long id) {
        super(id);
    }

}
