package com.flatrental.domain.announcement;

import com.flatrental.domain.announcement.attributes.furnishings.FurnishingItem;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Set;

@Entity
public class Room {

    @Id
    @GeneratedValue(generator = "ID_GENERATOR")

    @Positive
    private Integer personsNumber;

    @PositiveOrZero
    private String personsOccupied;

    @Positive
    private Double area;

    @Positive
    private Integer pricePerMounth;

//    @ManyToOne
//    private File mainPhoto;

    @ManyToMany
    private Set<FurnishingItem> furnishings;

    private Integer roomNumber;

}
