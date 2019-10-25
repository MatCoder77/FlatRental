package com.flatrental.domain.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Statistics {

    private long likesCounter;
    private long dislikesCounter;
    private long commentsCounter;
    private long favouritesCounter;
    private long viewsCounter;

    public void decrementCommentsCounterBy(int value) {
        commentsCounter = commentsCounter - value;
    }

    public void incrementCommentsCounter() {
        commentsCounter = commentsCounter + 1;
    }

    public void decrementFavouritesCounter() {
        favouritesCounter = favouritesCounter - 1;
    }

    public void incrementFavouritesCounter() {
        favouritesCounter = favouritesCounter + 1;
    }

    public void incrementViewsCounter() {
        viewsCounter = viewsCounter + 1;
    }

}
