package com.flatrental.domain.announcement.search;

import com.flatrental.domain.announcement.simpleattributes.apartmentamenities.ApartmentAmenity;
import com.flatrental.domain.announcement.simpleattributes.apartmentstate.ApartmentState;
import com.flatrental.domain.announcement.simpleattributes.buildingmaterial.BuildingMaterial;
import com.flatrental.domain.announcement.simpleattributes.buildingtype.BuildingType;
import com.flatrental.domain.announcement.simpleattributes.cookertype.CookerType;
import com.flatrental.domain.announcement.simpleattributes.furnishings.FurnishingItem;
import com.flatrental.domain.announcement.simpleattributes.heatingtype.HeatingType;
import com.flatrental.domain.announcement.simpleattributes.kitchentype.KitchenType;
import com.flatrental.domain.announcement.simpleattributes.neighbourhood.NeighbourhoodItem;
import com.flatrental.domain.announcement.simpleattributes.parkingtype.ParkingType;
import com.flatrental.domain.announcement.simpleattributes.preferences.Preference;
import com.flatrental.domain.announcement.simpleattributes.windowtype.WindowType;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class SearchCriteria {

    private Integer id;
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
    private Integer maxMaxFlo0rInBuilding;
    private Date minAvailableFrom;
    private Date maxAvailableFrom;
    private Set<BuildingType> allowedBuildingTypes;
    private Set<BuildingMaterial> allowedBuildingMaterials;
    private Set<HeatingType> allowedHeatingTypes;
    private Set<WindowType> allowedWindowTypes;
    private Set<ParkingType> allowedParkingTypes;
    private Set<ApartmentState> allowedApartmentStates;
    private Integer minYearBuilt;
    private Integer maxYearBuilt;
    private Boolean isWellPlanned;
    private Set<ApartmentAmenity> requiredApartmentAmenities;

    private Set<KitchenType> allowedKitchenTypes;
    private Integer minKitchenArea;
    private Integer maxKitchenArea;
    private Set<CookerType> allowedCookerTypes;
    private Set<FurnishingItem> requiredKitchenFurnishing;

    private Integer minNumberOfBathrooms;
    private Integer maxNumberOfBathrooms;
    private Boolean hasSeparatedWC;
    private Set<FurnishingItem> requiredBathroomFurnishing;


    private Set<Preference> requiredPreferences;
    private Set<NeighbourhoodItem> requiredNeighbourhoodItems;
    private Integer minNumberOfFlatmates;
    private Integer maxNumberOfFlatmates;

}
