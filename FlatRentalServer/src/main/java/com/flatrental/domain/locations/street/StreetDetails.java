package com.flatrental.domain.locations.street;

import com.flatrental.domain.locations.abstractlocality.AbstractLocality;

public class StreetDetails {

    private final Street street;
    private final AbstractLocality abstractLocality;

    public StreetDetails(Street street, AbstractLocality abstractLocality) {
        this.street = street;
        this.abstractLocality = abstractLocality;
    }

    public Street getStreet() {
        return street;
    }

    public AbstractLocality getAbstractLocality() {
        return abstractLocality;
    }

}
