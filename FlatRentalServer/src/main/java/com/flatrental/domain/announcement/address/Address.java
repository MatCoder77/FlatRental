package com.flatrental.domain.announcement.address;

import com.flatrental.domain.locations.abstractlocality.AbstractLocality;
import com.flatrental.domain.locations.commune.Commune;
import com.flatrental.domain.locations.district.District;
import com.flatrental.domain.locations.street.Street;
import com.flatrental.domain.locations.voivodeship.Voivodeship;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Embeddable
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    @ManyToOne
    private Voivodeship voivodeship;

    @ManyToOne
    private District district;

    @ManyToOne
    private Commune commune;

    @ManyToOne
    private AbstractLocality locality;

    @ManyToOne
    private AbstractLocality localityDistrict;

    @ManyToOne
    private AbstractLocality localityPart;

    @ManyToOne
    private Street street;

}
