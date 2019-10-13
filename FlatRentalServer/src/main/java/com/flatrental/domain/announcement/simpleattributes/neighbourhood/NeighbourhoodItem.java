package com.flatrental.domain.announcement.simpleattributes.neighbourhood;

import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@NoArgsConstructor
@Table(name = "NeighbourhoodItems")
public class NeighbourhoodItem {

    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    private Long id;

    @NaturalId
    private String name;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    private NeighbourhoodItem(Long id) {
        this.id = id;
    }

    public static NeighbourhoodItem fromId(Long id) {
        return new NeighbourhoodItem(id);
    }

}
