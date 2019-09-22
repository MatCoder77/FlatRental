package com.flatrental.domain.announcement;

import com.flatrental.domain.announcement.attributes.furnishings.FurnishingItem;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.Set;

@Embeddable
public class Bathroom {

    private Integer numberOfBathrooms;

    private Boolean separateWC;

    @OneToMany
    private Set<FurnishingItem> furnishing;

}
