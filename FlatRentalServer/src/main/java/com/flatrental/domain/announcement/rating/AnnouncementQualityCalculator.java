package com.flatrental.domain.announcement.rating;

import com.flatrental.domain.announcement.Announcement;
import com.flatrental.domain.announcement.bathroom.Bathroom;
import com.flatrental.domain.announcement.kitchen.Kitchen;
import com.flatrental.domain.file.File;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class AnnouncementQualityCalculator implements RatingCalculator {

    private static final long HIGH_IMPORTANCE_MULTIPLIER = 100;
    private static final long LOW_IMPORTANCE_MULTIPLIER = 10;
    private static final long DEFAULT_RATING = 0L;

    private static final long VERY_SHORT_DESCRIPTION_LENGTH = 100;
    private static final long SHORT_DESCRIPTION_LENGTH = 200;
    private static final long NORMAL_DESCRIPTION_LENGTH = 500;

    @Override
    public long calculateRating(Announcement announcement) {
        long result = DEFAULT_RATING;
        result += calculateRatingForField(announcement.getTitle(), 1);
        result += calculateRatingForField(announcement.getTotalArea(), 1);
        result += calculateRatingForField(announcement.getNumberOfRooms(), 3);
        result += calculateRatingForField(announcement.getPricePerMonth(), 6);
        result += calculateRatingForField(announcement.getAdditionalCostsPerMonth(), 2);
        result += calculateRatingForField(announcement.getSecurityDeposit(), 1);
        result += calculateRatingForField(announcement.getMaxFloorInBuilding(), 1);
        result += calculateRatingForField(announcement.getAvailableFrom(), 1);
        result += calculateRatingForField(announcement.getBuildingType(), 1);
        result += calculateRatingForField(announcement.getBuildingMaterial(), 1);
        result += calculateRatingForField(announcement.getHeatingType(), 2);
        result += calculateRatingForField(announcement.getWindowType(), 2);
        result += calculateRatingForField(announcement.getParkingType(), 1);
        result += calculateRatingForField(announcement.getApartmentState(), 1);
        result += calculateRatingForField(announcement.getYearBuilt(), 0.5);
        result += calculateRatingForField(announcement.getWellPlanned(), 3);
        result += calculateRatingForField(announcement.getApartmentAmenities(), 2);
        result += calculateRatingForField(Optional.ofNullable(announcement.getKitchen())
                .map(Kitchen::getKitchenType)
                .orElse(null), 2);
        result += calculateRatingForField(Optional.ofNullable(announcement.getKitchen())
                .map(Kitchen::getKitchenArea)
                .orElse(null), 0.5);
        result += calculateRatingForField(Optional.ofNullable(announcement.getKitchen())
                .map(Kitchen::getCookerType)
                .orElse(null), 1);
        result += calculateRatingForField(Optional.ofNullable(announcement.getKitchen())
                .map(Kitchen::getFurnishing)
                .orElse(null), 2);
        result += calculateRatingForField(Optional.ofNullable(announcement.getBathroom())
                .map(Bathroom::getNumberOfBathrooms)
                .orElse(null), 1);
        result += calculateRatingForField(Optional.ofNullable(announcement.getBathroom())
                .map(Bathroom::getSeparateWC)
                .orElse(null), 1);
        result += calculateRatingForField(Optional.ofNullable(announcement.getBathroom())
                .map(Bathroom::getFurnishing)
                .orElse(null), 2);
        result += calculateRatingForField(announcement.getPreferences(), 2);
        result += calculateRatingForField(announcement.getNeighbourhood(), 2);
        result += calculateRatingForDescription(announcement.getDescription(), 6);
        result += calculateImagesPoints(announcement.getAnnouncementImages(), 2);
        result += calculateRatingForField(announcement.getAboutRoommates(), 1);
        result += calculateRatingForField(announcement.getNumberOfFlatmates(), 1);
        return result;
    }

    private <T> long calculateRatingForField(T field, double importance) {
        return Optional.ofNullable(field)
                .map(f -> Math.round(HIGH_IMPORTANCE_MULTIPLIER * importance))
                .orElse(DEFAULT_RATING);
    }

    private <T> long calculateRatingForField(Set<T> field, double importance) {
        return Optional.ofNullable(field)
                .filter(f -> !f.isEmpty())
                .map(f -> Math.round(LOW_IMPORTANCE_MULTIPLIER * importance))
                .orElse(DEFAULT_RATING);
    }

    private long calculateRatingForDescription(String description, double importance) {
        return Optional.ofNullable(description)
                .map(desc -> getRatingForDescription(description, importance))
                .orElse(DEFAULT_RATING);
    }

    private long getRatingForDescription(String description, double importance) {
        if (description.length() < VERY_SHORT_DESCRIPTION_LENGTH) {
            return Math.round(2 * importance);
        }
        if (description.length() < SHORT_DESCRIPTION_LENGTH) {
            return Math.round(10 * importance);
        }
        if (description.length() < NORMAL_DESCRIPTION_LENGTH) {
            return Math.round(15 * importance);
        }
        return Math.round(20 * importance);
    }

    private long calculateImagesPoints(Map<Integer, File> images, double importance) {
        return Optional.ofNullable(images)
                .filter(imgs -> !imgs.isEmpty())
                .map(imgs -> getRatingForImages(imgs, importance))
                .orElse(DEFAULT_RATING);
    }

    private long getRatingForImages(Map<Integer, File> images, double importance) {
        if (images.size() == 1) {
            return Math.round(10 * importance);
        }
        if (images.size() < 3) {
            return Math.round(15 * importance);
        }
        return Math.round(20 * importance);
    }

}
