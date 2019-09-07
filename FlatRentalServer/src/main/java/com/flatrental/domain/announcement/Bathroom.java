package com.flatrental.domain.announcement;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Bathroom {

    private Boolean separatedWC;

    @Column(length = 500)
    private String bathroomDescription;

}
