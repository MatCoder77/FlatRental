package com.flatrental.domain.announcement.rating;

import com.flatrental.domain.announcement.Announcement;

public interface RatingCalculator {

    public long calculateRating(Announcement announcement);

}
