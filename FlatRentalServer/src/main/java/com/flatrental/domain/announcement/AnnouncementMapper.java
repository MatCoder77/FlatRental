package com.flatrental.domain.announcement;

import com.flatrental.api.AnnouncementBrowseDTO;
import com.flatrental.api.AnnouncementDTO;
import com.flatrental.api.AnnouncementSearchResultDTO;
import com.flatrental.api.FileDTO;
import com.flatrental.api.ResourceDTO;
import com.flatrental.api.SimpleAttributeDTO;
import com.flatrental.domain.announcement.address.AddressMapper;
import com.flatrental.domain.announcement.bathroom.BathroomMapper;
import com.flatrental.domain.announcement.kitchen.KitchenMapper;
import com.flatrental.domain.announcement.room.RoomMapper;
import com.flatrental.domain.announcement.search.SearchCriteria;
import com.flatrental.domain.announcement.simpleattribute.SimpleAttributeMapper;
import com.flatrental.domain.announcement.simpleattribute.apartmentamenity.ApartmentAmenity;
import com.flatrental.domain.announcement.simpleattribute.apartmentamenity.ApartmentAmenityService;
import com.flatrental.domain.announcement.simpleattribute.apartmentstate.ApartmentState;
import com.flatrental.domain.announcement.simpleattribute.apartmentstate.ApartmentStateService;
import com.flatrental.domain.announcement.simpleattribute.buildingmaterial.BuildingMaterial;
import com.flatrental.domain.announcement.simpleattribute.buildingmaterial.BuildingMaterialService;
import com.flatrental.domain.announcement.simpleattribute.buildingtype.BuildingType;
import com.flatrental.domain.announcement.simpleattribute.buildingtype.BuildingTypeService;
import com.flatrental.domain.announcement.simpleattribute.heatingtype.HeatingType;
import com.flatrental.domain.announcement.simpleattribute.heatingtype.HeatingTypeService;
import com.flatrental.domain.announcement.simpleattribute.neighbourhood.NeighbourhoodItem;
import com.flatrental.domain.announcement.simpleattribute.neighbourhood.NeighbourhoodItemService;
import com.flatrental.domain.announcement.simpleattribute.parkingtype.ParkingType;
import com.flatrental.domain.announcement.simpleattribute.parkingtype.ParkingTypeService;
import com.flatrental.domain.announcement.simpleattribute.preferences.Preference;
import com.flatrental.domain.announcement.simpleattribute.preferences.PreferenceService;
import com.flatrental.domain.announcement.simpleattribute.windowtype.WindowType;
import com.flatrental.domain.announcement.simpleattribute.windowtype.WindowTypeService;
import com.flatrental.domain.file.File;
import com.flatrental.domain.file.FileMapper;
import com.flatrental.domain.managedobject.ManagedObjectMapper;
import com.flatrental.domain.statistics.announcement.AnnouncementsStatisticsMapper;
import com.flatrental.domain.user.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.flatrental.infrastructure.utils.ResourcePaths.ID_PATH;

@Service
@RequiredArgsConstructor
public class AnnouncementMapper {

    private final AddressMapper addressMapper;
    private final RoomMapper roomMapper;
    private final KitchenMapper kitchenMapper;
    private final BathroomMapper bathroomMapper;
    private final AnnouncementsStatisticsMapper announcementsStatisticsMapper;
    private final ManagedObjectMapper managedObjectMapper;
    private final SimpleAttributeMapper simpleAttributeMapper;
    private final FileMapper fileMapper;
    private final UserMapper userMapper;
    private final BuildingTypeService buildingTypeService;
    private final BuildingMaterialService buildingMaterialService;
    private final HeatingTypeService heatingTypeService;
    private final WindowTypeService windowTypeService;
    private final ParkingTypeService parkingTypeService;
    private final ApartmentStateService apartmentStateService;
    private final ApartmentAmenityService apartmentAmenityService;
    private final PreferenceService preferenceService;
    private final NeighbourhoodItemService neighbourhoodItemService;

    public Announcement mapToAnnouncement(AnnouncementDTO announcementDTO) {
        if (announcementDTO == null) {
            return null;
        }
        return Announcement.builder()
                .id(announcementDTO.getId())
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
                .address(addressMapper.mapToAddress(announcementDTO.getAddress()))
                .buildingType(mapToBuildingType(announcementDTO.getBuildingType()))
                .buildingMaterial(mapToBuildingMaterial(announcementDTO.getBuildingMaterial()))
                .heatingType(mapToHeatingType(announcementDTO.getHeatingType()))
                .windowType(mapToWindowType(announcementDTO.getWindowType()))
                .parkingType(mapToParkingType(announcementDTO.getParkingType()))
                .apartmentState(mapToApartmentState(announcementDTO.getApartmentState()))
                .yearBuilt(announcementDTO.getYearBuilt())
                .wellPlanned(announcementDTO.getWellPlanned())
                .rooms(roomMapper.mapToRooms(announcementDTO.getRooms()))
                .kitchen(kitchenMapper.mapToKitchen(announcementDTO.getKitchen()))
                .bathroom(bathroomMapper.mapToBathroom(announcementDTO.getBathroom()))
                .description(announcementDTO.getDescription())
                .apartmentAmenities(mapToApartmentAmenities(announcementDTO.getApartmentAmenities()))
                .preferences(mapToPreferences(announcementDTO.getPreferences()))
                .neighbourhood(mapToNeighbourhoodItems(announcementDTO.getNeighbourhood()))
                .announcementImages(mapToAnnouncementImages(announcementDTO.getAnnouncementImages()))
                .aboutRoommates(announcementDTO.getAboutFlatmates())
                .numberOfFlatmates(announcementDTO.getNumberOfFlatmates())
                .build();
    }

    private Set<ApartmentAmenity> mapToApartmentAmenities(Collection<SimpleAttributeDTO> apartmentAmenityDTOs) {
        return simpleAttributeMapper.mapToSimpleAttributes(apartmentAmenityDTOs, apartmentAmenityService::getApartmentAmenities);
    }

    private Set<Preference> mapToPreferences(Collection<SimpleAttributeDTO> preferenceDTOs) {
        return simpleAttributeMapper.mapToSimpleAttributes(preferenceDTOs, preferenceService::getPreferences);
    }

    private Set<NeighbourhoodItem> mapToNeighbourhoodItems(Collection<SimpleAttributeDTO> neighbourhoodItemsDTOs) {
        return simpleAttributeMapper.mapToSimpleAttributes(neighbourhoodItemsDTOs, neighbourhoodItemService::getNeighbourhoodItems);
    }

    private Map<Integer, File> mapToAnnouncementImages(List<FileDTO> fileDTOs) {
        List<File> images = fileMapper.mapToFiles(fileDTOs);
        return IntStream.range(0, images.size())
                .boxed()
                .collect(Collectors.toMap(Function.identity(), images::get));
    }

    private BuildingType mapToBuildingType(SimpleAttributeDTO buildingType) {
        return simpleAttributeMapper.mapToSimpleAttribute(buildingType, buildingTypeService::getExistingBuildingType);
    }

    private BuildingMaterial mapToBuildingMaterial(SimpleAttributeDTO buildingMaterial) {
        return simpleAttributeMapper.mapToSimpleAttribute(buildingMaterial, buildingMaterialService::getExistingBuildingMaterial);
    }

    private HeatingType mapToHeatingType(SimpleAttributeDTO heatingType) {
        return simpleAttributeMapper.mapToSimpleAttribute(heatingType, heatingTypeService::getExistingHeatingType);
    }

    private WindowType mapToWindowType(SimpleAttributeDTO windowType) {
        return simpleAttributeMapper.mapToSimpleAttribute(windowType, windowTypeService::getExistingWindowType);
    }

    private ParkingType mapToParkingType(SimpleAttributeDTO parkingType) {
        return simpleAttributeMapper.mapToSimpleAttribute(parkingType, parkingTypeService::getExistingParkingType);
    }

    private ApartmentState mapToApartmentState(SimpleAttributeDTO apartmentState) {
        return simpleAttributeMapper.mapToSimpleAttribute(apartmentState, apartmentStateService::getExistingApartmentState);
    }

    public List<AnnouncementDTO> mapToAnnouncementDTOs(Collection<Announcement> announcements) {
        return Optional.ofNullable(announcements)
                .orElseGet(Collections::emptyList)
                .stream()
                .map(this::mapToAnnouncementDTO)
                .collect(Collectors.toList());
    }

    public AnnouncementDTO mapToAnnouncementDTO(Announcement announcement) {
        if (announcement == null) {
            return null;
        }
        return AnnouncementDTO.builder()
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
                .address(addressMapper.mapToAddressDTO(announcement.getAddress()))
                .yearBuilt(announcement.getYearBuilt())
                .wellPlanned(announcement.getWellPlanned())
                .rooms(roomMapper.mapToRoomDTOs(announcement.getRooms()))
                .kitchen(kitchenMapper.mapToKitchenDTO(announcement.getKitchen()))
                .bathroom(bathroomMapper.mapToBathroomDTO(announcement.getBathroom()))
                .description(announcement.getDescription())
                .apartmentAmenities(simpleAttributeMapper.mapToSimpleAttributeDTOs(announcement.getApartmentAmenities()))
                .preferences(simpleAttributeMapper.mapToSimpleAttributeDTOs(announcement.getPreferences()))
                .neighbourhood(simpleAttributeMapper.mapToSimpleAttributeDTOs(announcement.getNeighbourhood()))
                .announcementImages(mapToAnnouncementImageDTOs(announcement.getAnnouncementImages()))
                .aboutFlatmates(announcement.getAboutRoommates())
                .numberOfFlatmates(announcement.getNumberOfFlatmates())
                .info(managedObjectMapper.mapToManagedObjectDTO(announcement))
                .statistics(announcementsStatisticsMapper.mapToAnnouncementStatisticsDTO(announcement.getStatistics()))
                .buildingType(simpleAttributeMapper.mapToSimpleAttributeDTO(announcement.getBuildingType()))
                .buildingMaterial(simpleAttributeMapper.mapToSimpleAttributeDTO(announcement.getBuildingMaterial()))
                .heatingType(simpleAttributeMapper.mapToSimpleAttributeDTO(announcement.getHeatingType()))
                .windowType(simpleAttributeMapper.mapToSimpleAttributeDTO(announcement.getWindowType()))
                .parkingType(simpleAttributeMapper.mapToSimpleAttributeDTO(announcement.getParkingType()))
                .apartmentState(simpleAttributeMapper.mapToSimpleAttributeDTO(announcement.getApartmentState()))
                .build();
    }

    private List<FileDTO> mapToAnnouncementImageDTOs(Map<Integer, File> announcementImages) {
        return announcementImages.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .map(fileMapper::mapToFileDTO)
                .collect(Collectors.toList());
    }

    public AnnouncementSearchResultDTO mapToAnnouncementSearchResultDTO(Page<Announcement> announcementPage, SearchCriteria searchCriteria) {
        List<AnnouncementBrowseDTO> announcements = announcementPage.stream()
                .map(announcement -> mapToAnnouncementBrowseDTO(announcement))
                .collect(Collectors.toList());
        URI firstPageUri = generatePageUri(announcementPage.getPageable().first());
        URI previousPageUri = generatePageUri(announcementPage.getPageable().previousOrFirst());
        URI nextPageUri = generatePageUri(getNextOrLastPageable(announcementPage));
        URI lastPageUri = generatePageUri(getLastPageable(announcementPage));
        String pageWithSelectedNumber = generateUriTemplateForPageWithSelectedNumber(announcementPage.getPageable());
        return AnnouncementSearchResultDTO.builder()
                .announcements(announcements)
                .pageNumber((long) announcementPage.getNumber())
                .pageSize((long) announcementPage.getSize())
                .firstPage(firstPageUri)
                .previousPage(previousPageUri)
                .nextPage(nextPageUri)
                .lastPage(lastPageUri)
                .pageWithSelectedNumber(pageWithSelectedNumber)
                .totalSize(announcementPage.getTotalElements())
                .criteria(searchCriteria)
                .build();
    }

    private AnnouncementBrowseDTO mapToAnnouncementBrowseDTO(Announcement announcement) {
        return AnnouncementBrowseDTO.builder()
                .id(announcement.getId())
                .type(announcement.getType().toString().toLowerCase())
                .title(announcement.getTitle())
                .totalArea(announcement.getTotalArea())
                .numberOfRooms(announcement.getNumberOfRooms())
                .pricePerMonth(announcement.getPricePerMonth())
                .address(addressMapper.mapToAddressDTO(announcement.getAddress()))
                .rooms(roomMapper.mapToRoomBrowseDTOs(announcement.getRooms()))
                .announcementImages(mapToAnnouncementImageDTOs(announcement.getAnnouncementImages()))
                .statistics(announcementsStatisticsMapper.mapToAnnouncementStatisticsDTO(announcement.getStatistics()))
                .info(managedObjectMapper.mapToManagedObjectDTO(announcement))
                .build();
    }

    private URI generatePageUri(Pageable pageable) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(AnnouncementController.MAIN_RESOURCE)
                .path(AnnouncementController.SEARCH_RESOURCE)
                .queryParam(getQueryParamsForPageable(pageable))
                .build()
                .toUri();
    }

    private String getQueryParamsForPageable(Pageable pageable) {
        return getPaginationQueryParams(String.valueOf(pageable.getPageNumber()), pageable.getPageSize(), pageable.getSort());
    }

    private String getPaginationQueryParams(String pageNumber, int pageSize, Sort sort) {
        String pageNumberParam = "page=" + pageNumber;
        String pageSizeParam = "size=" + pageSize;
        String sortQueryParams = sort.stream()
                .map(this::mapOrderToQueryParam)
                .collect(Collectors.joining("&"));
        return pageNumberParam + "&" + pageSizeParam + "&" + sortQueryParams;
    }

    private String mapOrderToQueryParam(Sort.Order order) {
        return "sort=" + order.getProperty() + "," + order.getDirection().name();
    }

    private <T> Pageable getNextOrLastPageable(Page<T> page) {
        Pageable lastPageable = getLastPageable(page);
        Pageable nextPageable = page.getPageable().next();
        if (nextPageable.getPageNumber() > lastPageable.getPageNumber()) {
            return lastPageable;
        }
        return nextPageable;
    }

    private <T> Pageable getLastPageable(Page<T> page) {
        int maxPageNumber = Integer.max(page.getTotalPages() - 1, 0);
        return PageRequest.of(maxPageNumber, page.getPageable().getPageSize(), page.getSort());
    }

    private String generateUriTemplateForPageWithSelectedNumber(Pageable pageable) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(AnnouncementController.MAIN_RESOURCE)
                .path(AnnouncementController.SEARCH_RESOURCE)
                .toUriString() + "?" + getPaginationQueryParams("{pageNumber}", pageable.getPageSize(), pageable.getSort());

    }

    public ResourceDTO mapToResourceDTO(Announcement announcement) {
        if (announcement == null) {
            return null;
        }
        return ResourceDTO.builder()
                .id(announcement.getId())
                .uri(getAnnouncementUri(announcement))
                .build();
    }

    private URI getAnnouncementUri(Announcement announcement) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(AnnouncementController.MAIN_RESOURCE)
                .path(ID_PATH)
                .build(announcement.getId());
    }

}
