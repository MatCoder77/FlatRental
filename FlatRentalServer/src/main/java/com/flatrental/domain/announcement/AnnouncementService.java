package com.flatrental.domain.announcement;

import com.flatrental.api.AnnouncementDTO;
import com.flatrental.api.BathroomDTO;
import com.flatrental.api.FileDTO;
import com.flatrental.api.KitchenDTO;
import com.flatrental.api.ManagedObjectDTO;
import com.flatrental.api.RoomDTO;
import com.flatrental.api.SimpleResourceDTO;
import com.flatrental.domain.ManagedObjectState;
import com.flatrental.domain.announcement.address.AddressService;
import com.flatrental.domain.announcement.attributes.preferences.Preference;
import com.flatrental.domain.announcement.attributes.preferences.PreferenceService;
import com.flatrental.domain.announcement.attributes.apartmentamenities.ApartmentAmenity;
import com.flatrental.domain.announcement.attributes.apartmentamenities.ApartmentAmenityService;
import com.flatrental.domain.announcement.attributes.apartmentstate.ApartmentStateService;
import com.flatrental.domain.announcement.attributes.buildingmaterial.BuildingMaterialService;
import com.flatrental.domain.announcement.attributes.buildingtype.BuildingTypeService;
import com.flatrental.domain.announcement.attributes.cookertype.CookerTypeService;
import com.flatrental.domain.announcement.attributes.furnishings.FurnishingItem;
import com.flatrental.domain.announcement.attributes.furnishings.FurnishingService;
import com.flatrental.domain.announcement.attributes.heatingtype.HeatingTypeService;
import com.flatrental.domain.announcement.attributes.kitchentype.KitchenTypeService;
import com.flatrental.domain.announcement.attributes.neighbourhood.NeighbourhoodItem;
import com.flatrental.domain.announcement.attributes.neighbourhood.NeighbourhoodItemService;
import com.flatrental.domain.announcement.attributes.parkingtype.ParkingTypeService;
import com.flatrental.domain.announcement.attributes.windowtype.WindowTypeService;
import com.flatrental.domain.images.File;
import com.flatrental.domain.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class AnnouncementService {

    @Autowired
    private AddressService addressService;

    @Autowired
    private BuildingTypeService buildingTypeService;

    @Autowired
    private BuildingMaterialService buildingMaterialService;

    @Autowired
    private HeatingTypeService heatingTypeService;

    @Autowired
    private WindowTypeService windowTypeService;

    @Autowired
    private ParkingTypeService parkingTypeService;

    @Autowired
    private ApartmentStateService apartmentStateService;

    @Autowired
    private ApartmentAmenityService apartmentAmenityService;

    @Autowired
    private KitchenTypeService kitchenTypeService;

    @Autowired
    private CookerTypeService cookerTypeService;

    @Autowired
    private FurnishingService furnishingService;

    @Autowired
    private PreferenceService preferenceService;

    @Autowired
    private NeighbourhoodItemService neighbourhoodItemService;

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Autowired
    private UserService userService;

    public static final String NOT_FOUND = "There is no announcement with id {0}";

    public Announcement mapToAnnouncement(AnnouncementDTO announcementDTO) {
        return mapToAnnouncement(announcementDTO, new Announcement());
    }

    public Announcement mapToAnnouncement(AnnouncementDTO announcementDTO, Announcement announcement) {
        var builder = announcement.toBuilder()
                .type(AnnouncementType.fromString(announcementDTO.getType()))
                .title(announcementDTO.getTitle())
                .totalArea(announcementDTO.getTotalArea())
                .numberOfRooms(announcementDTO.getNumberOfRooms())
                .pricePerMonth(announcementDTO.getPricePerMonth())
                .additionalCostsPerMonth(announcementDTO.getAdditionalCostsPerMonth())
                .securityDeposit(announcementDTO.getSecurityDeposit())
                .floor(announcementDTO.getFloor())
                .maxFloorInBuilding(announcementDTO.getMaxFloorInBuilding())
                .availableFrom(announcementDTO.getAvailableFrom())
                .address(addressService.mapToAddress(announcementDTO.getAddress()))
                .yearBuilt(announcementDTO.getYearBuilt())
                .wellPlanned(announcementDTO.getWellPlanned())
                .rooms(getRooms(announcementDTO))
                .kitchen(getKitchen(announcementDTO))
                .bathroom(getBathroom(announcementDTO))
                .description(announcementDTO.getDescription())
                .apartmentAmenities(getApartmentAmenities(announcementDTO))
                .preferences(getPreferences(announcementDTO))
                .neighbourhood(getNeighbourhoodItems(announcementDTO))
                .announcementImages(getAnnouncementImages(announcementDTO))
                .aboutRoommates(announcementDTO.getAboutRoommates())
                .numberOfFlatmates(announcementDTO.getNumberOfFlatmates());

        Optional.ofNullable(announcementDTO.getBuildingType())
                .map(SimpleResourceDTO::getId)
                .map(buildingTypeService::getExsistingBuildingType)
                .ifPresent(builder::buildingType);
        Optional.ofNullable(announcementDTO.getBuildingMaterial())
                .map(SimpleResourceDTO::getId)
                .map(buildingMaterialService::getExistingBuildingMaterial)
                .ifPresent(builder::buildingMaterial);
        Optional.ofNullable(announcementDTO.getHeatingType())
                .map(SimpleResourceDTO::getId)
                .map(heatingTypeService::getExistingHeatingType)
                .ifPresent(builder::heatingType);
        Optional.ofNullable(announcementDTO.getWindowType())
                .map(SimpleResourceDTO::getId)
                .map(windowTypeService::getExistingWindowType)
                .ifPresent(builder::windowType);
        Optional.ofNullable(announcementDTO.getParkingType())
                .map(SimpleResourceDTO::getId)
                .map(parkingTypeService::getExistingParkingType)
                .ifPresent(builder::parkingType);
        Optional.ofNullable(announcementDTO.getApartmentState())
                .map(SimpleResourceDTO::getId)
                .map(apartmentStateService::getExistingApartmentState)
                .ifPresent(builder::apartmentState);

        return builder.build();
    }

    private Set<Room> getRooms(AnnouncementDTO announcementDTO) {
        List<Room> rooms = Optional.ofNullable(announcementDTO.getRooms())
                .orElse(Collections.emptyList())
                .stream()
                .map(this::mapToRoom)
                .collect(Collectors.toList());
        return new HashSet<>(rooms);
    }

    private Room mapToRoom(RoomDTO roomDTO) {
        return Room.builder()
                .numberOfPersons(roomDTO.getNumberOfPersons())
                .personsOccupied(roomDTO.getPersonsOccupied())
                .area(roomDTO.getArea())
                .pricePerMonth(roomDTO.getPricePerMonth())
                .furnishings(getFurnishing(roomDTO.getFurnishing()))
                .roomNumber(roomDTO.getRoomNumber())
                .build();
    }

    private Kitchen getKitchen(AnnouncementDTO announcementDTO) {
        KitchenDTO kitchenDTO = announcementDTO.getKitchen();
        if (kitchenDTO != null) {
            var kitchenBuilder = Kitchen.builder()
                    .kitchenArea(kitchenDTO.getKitchenArea())
                    .furnishing(getFurnishing(kitchenDTO.getFurnishing()));
            Optional.ofNullable(kitchenDTO.getKitchenType())
                    .map(SimpleResourceDTO::getId)
                    .map(kitchenTypeService::getExistingKitchenType)
                    .ifPresent(kitchenBuilder::kitchenType);
            Optional.ofNullable(kitchenDTO.getCookerType())
                    .map(SimpleResourceDTO::getId)
                    .map(cookerTypeService::getExistingCookerType)
                    .ifPresent(kitchenBuilder::cookerType);
            return kitchenBuilder.build();
        }
        return null;
    }

    private Set<FurnishingItem> getFurnishing(List<SimpleResourceDTO> furnishingDTOs) {
        List<FurnishingItem> furnishing = Optional.ofNullable(furnishingDTOs)
                .orElse(Collections.emptyList())
                .stream()
                .map(SimpleResourceDTO::getId)
                .collect(Collectors.collectingAndThen(Collectors.toList(), furnishingService::getFurnishingItems));
        return new HashSet<>(furnishing);
    }

    private Bathroom getBathroom(AnnouncementDTO announcementDTO) {
        BathroomDTO bathroomDTO = announcementDTO.getBathroom();
        if (bathroomDTO != null) {
            return Bathroom.builder()
                    .numberOfBathrooms(bathroomDTO.getNumberOfBathrooms())
                    .separateWC(bathroomDTO.getSeparateWC())
                    .furnishing(getFurnishing(bathroomDTO.getFurnishing()))
                    .build();
        }
        return null;
    }

    private Set<ApartmentAmenity> getApartmentAmenities(AnnouncementDTO announcementDTO) {
        List<ApartmentAmenity> apartmentAmenities = Optional.ofNullable(announcementDTO.getApartmentAmenities())
                .orElse(Collections.emptyList())
                .stream()
                .map(SimpleResourceDTO::getId)
                .collect(Collectors.collectingAndThen(Collectors.toList(), apartmentAmenityService::getApartmentAmenities));
        return new HashSet<>(apartmentAmenities);
    }

    private Set<Preference> getPreferences(AnnouncementDTO announcementDTO) {
        List<Preference> preferences = Optional.ofNullable(announcementDTO.getPreferences())
                .orElse(Collections.emptyList())
                .stream()
                .map(SimpleResourceDTO::getId)
                .collect(Collectors.collectingAndThen(Collectors.toList(), preferenceService::getPreferences));
        return new HashSet<>(preferences);
    }

    private Set<NeighbourhoodItem> getNeighbourhoodItems(AnnouncementDTO announcementDTO) {
        List<NeighbourhoodItem> neighbourhoodItems = Optional.ofNullable(announcementDTO.getNeighbourhood())
                .orElse(Collections.emptyList())
                .stream()
                .map(SimpleResourceDTO::getId)
                .collect(Collectors.collectingAndThen(Collectors.toList(), neighbourhoodItemService::getNeighbourhoodItems));
        return new HashSet<>(neighbourhoodItems);
    }

    private Map<Integer, File> getAnnouncementImages(AnnouncementDTO announcementDTO) {
        List<File> images = Optional.ofNullable(announcementDTO.getAnnouncementImages())
                .orElse(Collections.emptyList())
                .stream()
                .map(this::mapToFile)
                .collect(Collectors.toList());
        return IntStream.range(0, images.size())
                .boxed()
                .collect(Collectors.toMap(Function.identity(), images::get));
    }

    private File mapToFile(FileDTO fileDTO) {
        return new File(fileDTO.getFilename());
    }

    public Announcement createAnnouncement(AnnouncementDTO announcementDTO) {
        Announcement announcement = mapToAnnouncement(announcementDTO);
        announcement.setObjectState(ManagedObjectState.ACTIVE);
        return announcementRepository.save(announcement);
    }

    public Announcement getExistingAnnouncement(Long id) {
        return announcementRepository.findByIdAndObjectStateNot(id, ManagedObjectState.REMOVED)
                .orElseThrow(() -> new IllegalArgumentException(MessageFormat.format(NOT_FOUND, id)));
    }

    public AnnouncementDTO mapToAnnouncementDTO(Announcement announcement) {
        var builder = AnnouncementDTO.builder()
                .id(announcement.getId())
                .type(announcement.getType().toString().toLowerCase())
                .title(announcement.getTitle())
                .totalArea(announcement.getTotalArea())
                .numberOfRooms(announcement.getNumberOfRooms())
                .pricePerMonth(announcement.getPricePerMonth())
                .additionalCostsPerMonth(announcement.getAdditionalCostsPerMonth())
                .securityDeposit(announcement.getSecurityDeposit())
                .floor(announcement.getFloor())
                .maxFloorInBuilding(announcement.getMaxFloorInBuilding())
                .availableFrom(announcement.getAvailableFrom())
                .address(addressService.mapToAddressDTO(announcement.getAddress()))
                .yearBuilt(announcement.getYearBuilt())
                .wellPlanned(announcement.getWellPlanned())
                .rooms(getRoomDTOs(announcement))
                .kitchen(getKitchenDTO(announcement))
                .bathroom(getBathroomDTO(announcement))
                .description(announcement.getDescription())
                .apartmentAmenities(getApartmentAmenitiesDTOs(announcement))
                .preferences(getPreferencesDTOs(announcement))
                .neighbourhood(getNeighbourhoodDTOs(announcement))
                .announcementImages(getAnnouncementImagesDTOs(announcement))
                .aboutRoommates(announcement.getAboutRoommates())
                .numberOfFlatmates(announcement.getNumberOfFlatmates())
                .info(getManagedObjectDTO(announcement));

        Optional.ofNullable(announcement.getBuildingType())
                .map(buildingTypeService::mapToSimpleResourceDTO)
                .ifPresent(builder::buildingType);
        Optional.ofNullable(announcement.getBuildingMaterial())
                .map(buildingMaterialService::mapToSimpleResourceDTO)
                .ifPresent(builder::buildingMaterial);
        Optional.ofNullable(announcement.getHeatingType())
                .map(heatingTypeService::mapToSimpleResourceDTO)
                .ifPresent(builder::heatingType);
        Optional.ofNullable(announcement.getWindowType())
                .map(windowTypeService::mapToSimpleResourceDTO)
                .ifPresent(builder::windowType);
        Optional.ofNullable(announcement.getParkingType())
                .map(parkingTypeService::mapToSimpleResourceDTO)
                .ifPresent(builder::parkingType);
        Optional.ofNullable(announcement.getApartmentState())
                .map(apartmentStateService::mapToSimpleResourceDTO)
                .ifPresent(builder::apartmentState);

        return builder.build();
    }

    private List<RoomDTO> getRoomDTOs(Announcement announcement) {
        return Optional.ofNullable(announcement.getRooms())
                .orElse(Collections.emptySet())
                .stream()
                .sorted(Comparator.comparingInt(Room::getRoomNumber))
                .map(this::mapToRoomDTO)
                .collect(Collectors.toList());
    }

    private RoomDTO mapToRoomDTO(Room room) {
        return RoomDTO.builder()
                .id(room.getId())
                .numberOfPersons(room.getNumberOfPersons())
                .personsOccupied(room.getPersonsOccupied())
                .area(room.getArea())
                .pricePerMonth(room.getPricePerMonth())
                .furnishing(getFurnishingDTOs(room.getFurnishings()))
                .roomNumber(room.getRoomNumber())
                .build();
    }

    private List<SimpleResourceDTO> getFurnishingDTOs(Collection<FurnishingItem> furnishingItems) {
        return Optional.ofNullable(furnishingItems)
                .orElse(Collections.emptyList())
                .stream()
                .map(furnishingService::mapToSimpleResourceDTO)
                .collect(Collectors.toList());
    }

    private KitchenDTO getKitchenDTO(Announcement announcement) {
        Kitchen kitchen = announcement.getKitchen();
        if (kitchen != null) {
            var kitchenBuilder = KitchenDTO.builder()
                    .kitchenArea(kitchen.getKitchenArea())
                    .furnishing(getFurnishingDTOs(kitchen.getFurnishing()));
            Optional.ofNullable(kitchen.getKitchenType())
                    .map(kitchenTypeService::mapToSimpleResourceDTO)
                    .ifPresent(kitchenBuilder::kitchenType);
            Optional.ofNullable(kitchen.getCookerType())
                    .map(cookerTypeService::mapToSimpleResourceDTO)
                    .ifPresent(kitchenBuilder::cookerType);
            return kitchenBuilder.build();
        }
        return null;
    }

    private BathroomDTO getBathroomDTO(Announcement announcement) {
        Bathroom bathroom = announcement.getBathroom();
        if (bathroom != null) {
            return BathroomDTO.builder()
                    .numberOfBathrooms(bathroom.getNumberOfBathrooms())
                    .separateWC(bathroom.getSeparateWC())
                    .furnishing(getFurnishingDTOs(bathroom.getFurnishing()))
                    .build();
        }
        return null;
    }

    private List<SimpleResourceDTO> getApartmentAmenitiesDTOs(Announcement announcement) {
        return Optional.ofNullable(announcement.getApartmentAmenities())
                .orElse(Collections.emptySet())
                .stream()
                .map(apartmentAmenityService::mapToSimpleResourceDTO)
                .collect(Collectors.toList());
    }

    private List<SimpleResourceDTO> getPreferencesDTOs(Announcement announcement) {
        return Optional.ofNullable(announcement.getPreferences())
                .orElse(Collections.emptySet())
                .stream()
                .map(preferenceService::mapToSimpleResourceDTO)
                .collect(Collectors.toList());
    }

    private List<SimpleResourceDTO> getNeighbourhoodDTOs(Announcement announcement) {
        return Optional.ofNullable(announcement.getNeighbourhood())
                .orElse(Collections.emptySet())
                .stream()
                .map(neighbourhoodItemService::mapToSimpleResourceDTO)
                .collect(Collectors.toList());
    }

    private List<FileDTO> getAnnouncementImagesDTOs(Announcement announcement) {
        return Optional.ofNullable(announcement.getAnnouncementImages())
                .orElse(Collections.emptyMap())
                .entrySet()
                .stream()
                .sorted(Comparator.comparingInt(Map.Entry::getKey))
                .map(Map.Entry::getValue)
                .map(this::mapToFileDTO)
                .collect(Collectors.toList());
    }

    private FileDTO mapToFileDTO(File file) {
        return new FileDTO(file.getFilename());
    }

    private ManagedObjectDTO getManagedObjectDTO(Announcement announcement) {
        return ManagedObjectDTO.builder()
                .createdAt(announcement.getCreatedAt())
                .createdBy(userService.mapToUserDTO(announcement.getCreatedBy()))
                .updatedAt(announcement.getUpdatedAt())
                .updatedBy(userService.mapToUserDTO(announcement.getUpdatedBy()))
                .objectState(announcement.getObjectState().name())
                .build();
    }

    public Announcement updateAnnouncement(Announcement existingAnnouncement, AnnouncementDTO updatedAnnouncementDTO) {
        Announcement updatedAnnouncement = mapToExistingAnnouncement(updatedAnnouncementDTO, existingAnnouncement);
        return announcementRepository.save(updatedAnnouncement);
    }

    public Announcement mapToExistingAnnouncement(AnnouncementDTO announcementDTO, Announcement existingAnnouncement) {
        Objects.requireNonNull(existingAnnouncement);
        return mapToAnnouncement(announcementDTO, existingAnnouncement);
    }

    public boolean removeAnnouncement(Announcement announcement) {
        announcement.setObjectState(ManagedObjectState.REMOVED);
        announcementRepository.save(announcement);
        return true;
    }

}
