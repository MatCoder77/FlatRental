package com.flatrental.domain.announcement;

import javax.persistence.Embeddable;

@Embeddable
public class AnnouncementStatistics {

    private Long views;
    private Long favourites;

}
