package com.flatrental.domain.announcement;

import com.flatrental.domain.announcement.search.SearchCriteria;
import com.flatrental.domain.managedobject.ManagedObjectState;
import com.flatrental.domain.permissions.PermissionsValidationService;
import com.flatrental.domain.statistics.announcement.AnnouncementStatistics;
import com.flatrental.domain.user.User;
import com.flatrental.infrastructure.security.UserInfo;
import com.flatrental.infrastructure.utils.Exceptions;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final AnnouncementQualityCalculator announcementQualityCalculator;
    private final PermissionsValidationService permissionsValidationService;

    public Announcement getExistingAnnouncementAndIncrementViewsCounter(Long id) {
        Announcement announcement = getExistingAnnouncement(id);
        announcement.getStatistics().incrementViewsCounter();
        return announcement;
    }

    public Announcement getExistingAnnouncement(Long id) {
        return announcementRepository.findByIdAndObjectStateNot(id, ManagedObjectState.REMOVED)
                .orElseThrow(() -> Exceptions.getObjectNotFoundException(Announcement.class, id,
                        Set.of(ManagedObjectState.ACTIVE, ManagedObjectState.INACTIVE)));
    }

    public Announcement createAnnouncement(Announcement announcement) {
        prepareAnnouncementBeforeCreate(announcement);
        return announcementRepository.save(announcement);
    }

    private void prepareAnnouncementBeforeCreate(Announcement announcement) {
        announcement.setId(null);
        announcement.setObjectState(ManagedObjectState.ACTIVE);
        announcement.setQuality(announcementQualityCalculator.calculateAnnouncementQuality(announcement));
        announcement.setStatistics(new AnnouncementStatistics());
    }

    public Announcement updateAnnouncement(Long id, Announcement updatedAnnouncement, UserInfo userInfo) {
        Announcement announcement = getExistingAnnouncement(id);
        permissionsValidationService.validatePermissionToEditAnnouncement(userInfo, announcement);
        setUpdatableAttributesBasedOnUpdatedAnnouncement(announcement, updatedAnnouncement);
        return announcement;
    }

    private void setUpdatableAttributesBasedOnUpdatedAnnouncement(Announcement existingAnnouncement, Announcement announcementToUpdate) {
        existingAnnouncement.setTitle(announcementToUpdate.getTitle());
        existingAnnouncement.setTotalArea(announcementToUpdate.getTotalArea());
        existingAnnouncement.setNumberOfRooms(announcementToUpdate.getNumberOfRooms());
        existingAnnouncement.setPricePerMonth(announcementToUpdate.getPricePerMonth());
        existingAnnouncement.setAdditionalCostsPerMonth(announcementToUpdate.getAdditionalCostsPerMonth());
        existingAnnouncement.setSecurityDeposit(announcementToUpdate.getSecurityDeposit());
        existingAnnouncement.setFloor(announcementToUpdate.getFloor());
        existingAnnouncement.setMaxFloorInBuilding(announcementToUpdate.getMaxFloorInBuilding());
        existingAnnouncement.setAvailableFrom(announcementToUpdate.getAvailableFrom());
        existingAnnouncement.setAddress(announcementToUpdate.getAddress());
        existingAnnouncement.setYearBuilt(announcementToUpdate.getYearBuilt());
        existingAnnouncement.setWellPlanned(announcementToUpdate.getWellPlanned());
        existingAnnouncement.setBuildingType(announcementToUpdate.getBuildingType());
        existingAnnouncement.setBuildingMaterial(announcementToUpdate.getBuildingMaterial());
        existingAnnouncement.setHeatingType(announcementToUpdate.getHeatingType());
        existingAnnouncement.setWindowType(announcementToUpdate.getWindowType());
        existingAnnouncement.setParkingType(announcementToUpdate.getParkingType());
        existingAnnouncement.setApartmentState(announcementToUpdate.getApartmentState());
        existingAnnouncement.setRooms(announcementToUpdate.getRooms());
        existingAnnouncement.setKitchen(announcementToUpdate.getKitchen());
        existingAnnouncement.setBathroom(announcementToUpdate.getBathroom());
        existingAnnouncement.setDescription(announcementToUpdate.getDescription());
        existingAnnouncement.setApartmentAmenities(announcementToUpdate.getApartmentAmenities());
        existingAnnouncement.setPreferences(announcementToUpdate.getPreferences());
        existingAnnouncement.setNeighbourhood(announcementToUpdate.getNeighbourhood());
        existingAnnouncement.setAnnouncementImages(announcementToUpdate.getAnnouncementImages());
        existingAnnouncement.setAboutRoommates(announcementToUpdate.getAboutRoommates());
        existingAnnouncement.setNumberOfFlatmates(announcementToUpdate.getNumberOfFlatmates());
    }

    public void removeAnnouncement(Long id, UserInfo userInfo) {
        Announcement announcement = getExistingAnnouncement(id);
        permissionsValidationService.validatePermissionToEditAnnouncement(userInfo, announcement);
        changeAnnouncementState(announcement, ManagedObjectState.REMOVED);
    }

    private void changeAnnouncementState(Announcement announcement, ManagedObjectState managedObjectState) {
        announcement.setObjectState(managedObjectState);
    }

    public void changeAnnouncementState(Long id, ManagedObjectState objectState, UserInfo userInfo) {
        Announcement announcement = getExistingAnnouncement(id);
        permissionsValidationService.validatePermissionsToChangeAnnouncementState(userInfo, objectState, announcement);
        changeAnnouncementState(announcement, objectState);
    }

    public Page<Announcement> searchAnnouncements(SearchCriteria searchCriteria, Pageable pageable) {
        return announcementRepository.searchAnnouncementsByCriteria(searchCriteria, pageable);
    }

    public boolean hasPermissionToEditAnnouncement(Long id, UserInfo userInfo) {
        Announcement announcement = getExistingAnnouncement(id);
        return permissionsValidationService.hasPermissionToEditAnnouncement(userInfo, announcement);
    }

    public void incrementCommentsCounter(Announcement announcement) {
        announcement.getStatistics().incrementCommentsCounter();
        announcementRepository.save(announcement);
    }

    public void decrementCommentsCounter(Announcement announcement, int numberOfRemovedComments) {
        announcement.getStatistics().decrementCommentsCounterBy(numberOfRemovedComments);
        announcementRepository.save(announcement);
    }

    public void addAnnouncementsToFavourites(Long id, User user) {
        Announcement announcement = getExistingAnnouncement(id);
        if (!user.getFavourites().contains(announcement)) {
            user.addAnnouncementToFavourites(announcement);
            announcement.getStatistics().incrementFavouritesCounter();
        }
    }

    public void removeAnnouncementsFromFavourites(Long id, User user) {
        Announcement announcement = getExistingAnnouncement(id);
        if (user.getFavourites().contains(announcement)) {
            user.removeAnnouncementFromFavourites(announcement);
            announcement.getStatistics().decrementFavouritesCounter();
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void setAllAnnouncementsOlderThanMonthAsInactive() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        Instant monthAgo = cal.getTime().toInstant();
        List<Announcement> announcementsToDeactivate = announcementRepository.getAllByUpdatedAtBeforeAndObjectState(monthAgo, ManagedObjectState.ACTIVE);
        announcementsToDeactivate.forEach(announcement -> announcement.setObjectState(ManagedObjectState.INACTIVE));
        announcementRepository.saveAll(announcementsToDeactivate);
    }

}
