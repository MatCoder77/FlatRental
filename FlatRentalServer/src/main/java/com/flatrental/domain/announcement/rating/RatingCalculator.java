package com.flatrental.domain.announcement.rating;

import com.flatrental.domain.announcement.Announcement;

public interface RatingCalculator {

    long calculateRating(Announcement announcement);

}
