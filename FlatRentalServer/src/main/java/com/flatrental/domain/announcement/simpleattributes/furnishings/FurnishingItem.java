package com.flatrental.domain.announcement.simpleattributes.furnishings;

import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@NoArgsConstructor
@Table(name = "FurnishingItems")
public class FurnishingItem {

    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    private Long id;

    @NotNull
    private String name;

    @Column(name = "furnishing_type")
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

    private FurnishingItem(Long id) {
        this.id = id;
    }

    public static FurnishingItem fromId(Long id) {
        return new FurnishingItem(id);
    }

}
