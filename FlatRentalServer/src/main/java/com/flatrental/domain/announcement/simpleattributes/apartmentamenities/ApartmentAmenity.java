package com.flatrental.domain.announcement.simpleattributes.apartmentamenities;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.NaturalId;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@NoArgsConstructor
@Table(name = "ApartmentAmenities")
@Immutable
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "apartmentAmenityCache")
public class ApartmentAmenity {

    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    private Long id;

    @NaturalId
    private String name;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    private ApartmentAmenity(Long id) {
        this.id = id;
    }

    public static ApartmentAmenity fromId(Long id) {
        return new ApartmentAmenity(id);
    }

}
