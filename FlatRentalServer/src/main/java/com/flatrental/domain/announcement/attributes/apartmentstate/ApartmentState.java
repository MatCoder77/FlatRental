package com.flatrental.domain.announcement.attributes.apartmentstate;

import org.hibernate.annotations.NaturalId;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ApartmentStates")
public class ApartmentState {

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
