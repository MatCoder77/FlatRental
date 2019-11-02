package com.flatrental.domain.announcement;

import com.flatrental.domain.file.File;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class AnnouncementQualityCalculator {

    public long calculateAnnouncementQuality(Announcement announcement) {
        int result = 0;

        result += getPointsForField(announcement.getTitle(), 1);
        result += getPointsForField(announcement.getTotalArea(), 1);
        result += getPointsForField(announcement.getNumberOfRooms(), 3);
        result += getPointsForField(announcement.getPricePerMonth(), 6);
        result += getPointsForField(announcement.getAdditionalCostsPerMonth(), 2);
        result += getPointsForField(announcement.getSecurityDeposit(), 1);
        result += getPointsForField(announcement.getMaxFloorInBuilding(), 1);
        result += getPointsForField(announcement.getAvailableFrom(), 1);

        result += getPointsForField(announcement.getBuildingType(), 1);
        result += getPointsForField(announcement.getBuildingMaterial(), 1);
        result += getPointsForField(announcement.getHeatingType(), 2);
        result += getPointsForField(announcement.getWindowType(), 2);
        result += getPointsForField(announcement.getParkingType(), 1);
        result += getPointsForField(announcement.getApartmentState(), 1);
        result += getPointsForField(announcement.getYearBuilt(), 0.5);
        result += getPointsForField(announcement.getWellPlanned(), 3);
        result += getPointsForField(announcement.getApartmentAmenities(), 2);


        result += getPointsForField(Optional.ofNullable(announcement.getKitchen())
                .map(Kitchen::getKitchenType)
                .orElse(null), 2);
        result += getPointsForField(Optional.ofNullable(announcement.getKitchen())
                .map(Kitchen::getKitchenArea)
                .orElse(null), 0.5);
        result += getPointsForField(Optional.ofNullable(announcement.getKitchen())
                .map(Kitchen::getCookerType)
                .orElse(null), 1);
        result += getPointsForField(Optional.ofNullable(announcement.getKitchen())
                .map(Kitchen::getFurnishing)
                .orElse(null), 2);

        result += getPointsForField(Optional.ofNullable(announcement.getBathroom())
                .map(Bathroom::getNumberOfBathrooms)
                .orElse(null), 1);
        result += getPointsForField(Optional.ofNullable(announcement.getBathroom())
                .map(Bathroom::getSeparateWC)
                .orElse(null), 1);
        result += getPointsForField(Optional.ofNullable(announcement.getBathroom())
                .map(Bathroom::getFurnishing)
                .orElse(null), 2);

        result += getPointsForField(announcement.getPreferences(), 2);
        result += getPointsForField(announcement.getNeighbourhood(), 2);
        result += calculateDescriptionPoints(announcement.getDescription(), 6);
        result += calculateImagesPoints(announcement.getAnnouncementImages(), 2);
        result += getPointsForField(announcement.getAboutRoommates(), 1);
        result += getPointsForField(announcement.getNumberOfFlatmates(), 1);

        return result;
    }

    private <T> long getPointsForField(T field, double importance) {
        if (field != null) {
            return Math.round(100 * importance);
        }
        return 0;
    }

    private <T> long getPointsForField(Set<T> field, double importance) {
        if (field != null && !field.isEmpty()) {
            return Math.round(10 * importance);
        }
        return 0;
    }

    private long calculateDescriptionPoints(String description, double importance) {
        if (description != null) {
            if (description.length() < 100) {
                return Math.round(2 * importance);
            }
            if (description.length() < 200) {
                return Math.round(10 * importance);
            }
            if (description.length() < 500) {
                return Math.round(15 * importance);
            }
            return Math.round(20 * importance);
        }
        return 0;
    }

    private long calculateImagesPoints(Map<Integer, File> images, double importance) {
        if (images != null && !images.isEmpty()) {
            if (images.size() == 1) {
                return Math.round(10 * importance);
            }
            if (images.size() < 3) {
                return Math.round(15 * importance);
            }
            if (images.size() < 5) {
                return Math.round(20 * importance);
            }
            return Math.round(20 * importance);
        }
        return 0;
    }

}
