package com.flatrental.domain.announcement;

import com.flatrental.domain.furnishings.Furnishings;
import com.flatrental.domain.images.Image;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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
//    private Image mainPhoto;

    @ManyToMany
    private Set<Furnishings> furnishings;

    private Integer roomNumber;

}
