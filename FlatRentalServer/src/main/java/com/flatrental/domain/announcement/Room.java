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
    private Long id;

    @Positive
    private Integer numberOfPersons;

    @PositiveOrZero
    private String personsOccupied;

    @Positive
    private Double area;

    @Positive
    private Integer pricePerMounth;

    @ManyToMany
    private Set<FurnishingItem> furnishings;

    private Integer roomNumber;

}
