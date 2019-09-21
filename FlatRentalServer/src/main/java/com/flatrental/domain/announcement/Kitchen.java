package com.flatrental.domain.announcement;

import com.flatrental.domain.announcement.attributes.cookertype.CookerType;
import com.flatrental.domain.announcement.attributes.furnishings.FurnishingItem;
import com.flatrental.domain.announcement.attributes.kitchentype.KitchenType;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Set;

@Embeddable
public class Kitchen {

    @ManyToOne
    private KitchenType kitchenType;

    private Integer kitchenArea;

    @ManyToOne
    private CookerType cookerType;

    @OneToMany
    private Set<FurnishingItem> furnishing;

}
