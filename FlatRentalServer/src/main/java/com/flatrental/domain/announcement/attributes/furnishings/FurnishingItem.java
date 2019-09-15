package com.flatrental.domain.announcement.attributes.furnishings;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class FurnishingItem {

    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    private Long id;

    @NotNull
    private String name;

    @Enumerated(EnumType.STRING)
    private FurnishingType furnishingType;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public FurnishingType getFurnishingType() {
        return furnishingType;
    }
}
