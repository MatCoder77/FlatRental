package com.flatrental.domain.announcement.search;

import lombok.Data;

import java.util.Set;

@Data
public class RoomCriteria {

    private Integer minNumberOfPersons;
    private Integer maxNumberOfPersons;
    private Integer minPersonsOccupied;
    private Integer maxPersonsOccupied;
    private Integer minArea;
    private Integer maxArea;
    private Set<Long> requiredFurnishing;

}
