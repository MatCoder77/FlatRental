package com.flatrental.domain.announcement;

import com.flatrental.domain.cookertype.CookerType;
import com.flatrental.domain.kitchentype.KitchenType;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Embeddable
public class Kitchen {

    @ManyToOne
    private KitchenType kitchenType;

    private Double kitchenArea;

    @ManyToOne
    private CookerType cookerType;

    @Column(length = 500)
    private String kitchenDescription;

}
