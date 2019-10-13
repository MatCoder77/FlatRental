package com.flatrental.domain.announcement.search;

import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class SearchCriteria {

    private Long id;
    private String announcementType;
    private Integer minTotalArea;
    private Integer maxTotalArea;
    private Integer minNumberOfRooms;
    private Integer maxNumberOfRooms;
    private Integer minPricePerMonth;
    private Integer maxPricePerMonth;
    private Integer minAdditionalCostsPerMonth;
    private Integer maxAdditionalCostsPerMonth;
    private Integer minSecurityDeposit;
    private Integer maxSecurityDeposit;
    private Integer minFloor;
    private Integer maxFloor;
    private Integer minMaxFloorInBuilding;
    private Integer maxMaxFloorInBuilding;
    private Date minAvailableFrom;
    private Date maxAvailableFrom;
    private Set<Long> allowedBuildingTypes;
    private Set<Long> allowedBuildingMaterials;
    private Set<Long> allowedHeatingTypes;
    private Set<Long> allowedWindowTypes;
    private Set<Long> allowedParkingTypes;
    private Set<Long> allowedApartmentStates;
    private Integer minYearBuilt;
    private Integer maxYearBuilt;
    private Boolean isWellPlanned;
    private Set<Long> requiredApartmentAmenities;

    private Set<Long> allowedKitchenTypes;
    private Integer minKitchenArea;
    private Integer maxKitchenArea;
    private Set<Long> allowedCookerTypes;
    private Set<Long> requiredKitchenFurnishing;

    private Integer minNumberOfBathrooms;
    private Integer maxNumberOfBathrooms;
    private Boolean hasSeparatedWC;
    private Set<Long> requiredBathroomFurnishing;


    private Set<Long> requiredPreferences;
    private Set<Long> requiredNeighbourhoodItems;
    private Integer minNumberOfFlatmates;
    private Integer maxNumberOfFlatmates;


}
