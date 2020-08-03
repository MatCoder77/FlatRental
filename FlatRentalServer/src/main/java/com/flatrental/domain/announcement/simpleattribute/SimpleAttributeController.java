package com.flatrental.domain.announcement.simpleattribute;

import com.flatrental.api.SimpleAttributeDTO;
import com.flatrental.domain.announcement.simpleattribute.apartmentamenity.ApartmentAmenityService;
import com.flatrental.domain.announcement.simpleattribute.apartmentstate.ApartmentStateService;
import com.flatrental.domain.announcement.simpleattribute.buildingmaterial.BuildingMaterialService;
import com.flatrental.domain.announcement.simpleattribute.buildingtype.BuildingTypeService;
import com.flatrental.domain.announcement.simpleattribute.cookertype.CookerTypeService;
import com.flatrental.domain.announcement.simpleattribute.furnishings.FurnishingService;
import com.flatrental.domain.announcement.simpleattribute.furnishings.FurnishingType;
import com.flatrental.domain.announcement.simpleattribute.heatingtype.HeatingTypeService;
import com.flatrental.domain.announcement.simpleattribute.kitchentype.KitchenTypeService;
import com.flatrental.domain.announcement.simpleattribute.neighbourhood.NeighbourhoodItemService;
import com.flatrental.domain.announcement.simpleattribute.parkingtype.ParkingTypeService;
import com.flatrental.domain.announcement.simpleattribute.preferences.PreferenceService;
import com.flatrental.domain.announcement.simpleattribute.windowtype.WindowTypeService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Api(tags = "Simple Attributes")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SimpleAttributeController {

    private static final String FURNISHING_TYPE_PARAM = "type";

    private final ApartmentAmenityService apartmentAmenityService;
    private final ApartmentStateService apartmentStateService;
    private final BuildingMaterialService buildingMaterialService;
    private final BuildingTypeService buildingTypeService;
    private final CookerTypeService cookerTypeService;
    private final FurnishingService furnishingService;
    private final HeatingTypeService heatingTypeService;
    private final KitchenTypeService kitchenTypeService;
    private final NeighbourhoodItemService neighbourhoodItemService;
    private final ParkingTypeService parkingTypeService;
    private final PreferenceService preferenceService;
    private final WindowTypeService windowTypeService;
    private final SimpleAttributeMapper simpleAttributeMapper;
    
    @GetMapping("/apartmentamenity")
    public List<SimpleAttributeDTO> getAllApartmentAmenityTypes() {
        return apartmentAmenityService.getAllApartmentAmenityTypes().stream()
                .map(simpleAttributeMapper::mapToSimpleAttributeDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/apartmentstate")
    public List<SimpleAttributeDTO> getApartmentStateTypes() {
        return apartmentStateService.getAllApartmentStateTypes().stream()
                .map(simpleAttributeMapper::mapToSimpleAttributeDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/buildingmaterial")
    public List<SimpleAttributeDTO> getBuildingMaterials() {
        return buildingMaterialService.getAllBuildingMaterials().stream()
                .map(simpleAttributeMapper::mapToSimpleAttributeDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/buildingtype")
    public List<SimpleAttributeDTO> getAllBuildingTypes() {
        return buildingTypeService.getAllBuildingTypes().stream()
                .map(simpleAttributeMapper::mapToSimpleAttributeDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/cookertype")
    public List<SimpleAttributeDTO> getCookerTypes() {
        return cookerTypeService.getAllCookerTypes().stream()
                .map(simpleAttributeMapper::mapToSimpleAttributeDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/furnishing")
    public List<SimpleAttributeDTO> getFurnishing(@RequestParam(FURNISHING_TYPE_PARAM) Optional<FurnishingType> furnishingType) {
        return furnishingType.map(furnishingService::getFurnishingItemsWithType)
                .orElseGet(furnishingService::getFurnishingItems)
                .stream()
                .map(simpleAttributeMapper::mapToSimpleAttributeDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/heatingtype")
    public List<SimpleAttributeDTO> getHeatingTypes() {
        return heatingTypeService.getAllHeatingTypes().stream()
                .map(simpleAttributeMapper::mapToSimpleAttributeDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/kitchentype")
    public List<SimpleAttributeDTO> getKitchenTypes() {
        return kitchenTypeService.getAllKitchenTypes().stream()
                .map(simpleAttributeMapper::mapToSimpleAttributeDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/neighbourhood")
    public List<SimpleAttributeDTO> getNeighborhoodItems() {
        return neighbourhoodItemService.getAllNeighbourItems()
                .stream()
                .map(simpleAttributeMapper::mapToSimpleAttributeDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/parkingtype")
    public List<SimpleAttributeDTO> getParkingTypes() {
        return parkingTypeService.getAllParkingTypes().stream()
                .map(simpleAttributeMapper::mapToSimpleAttributeDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/preferences")
    public List<SimpleAttributeDTO> getAllPreferences() {
        return preferenceService.getAllPreferences()
                .stream()
                .map(simpleAttributeMapper::mapToSimpleAttributeDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/windowtype")
    public List<SimpleAttributeDTO> getWindowTypes() {
        return windowTypeService.getAllWindowTypes().stream()
                .map(simpleAttributeMapper::mapToSimpleAttributeDTO)
                .collect(Collectors.toList());
    }

}
