package com.flatrental.domain.locations.street;

import com.flatrental.domain.locations.abstractlocality.AbstractLocality;

public class StreetAndAssociatedLocality {

    private Street street;
    private AbstractLocality abstractLocality;

    public StreetAndAssociatedLocality(Street street, AbstractLocality abstractLocality) {
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
