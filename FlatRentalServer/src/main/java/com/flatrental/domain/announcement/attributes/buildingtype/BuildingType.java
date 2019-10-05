package com.flatrental.domain.announcement.attributes.buildingtype;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "BuildingTypes")
public class BuildingType {

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

}
