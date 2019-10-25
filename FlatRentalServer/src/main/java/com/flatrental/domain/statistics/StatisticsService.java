package com.flatrental.domain.statistics;

import com.flatrental.api.StatisticsDTO;
import org.springframework.stereotype.Service;

@Service
public class StatisticsService {

    public StatisticsDTO mapToStatisticsDTO(Statistics statistics) {
        return StatisticsDTO.builder()
                .commentsCounter(statistics.getCommentsCounter())
                .likesCounter(statistics.getLikesCounter())
                .dislikesCounter(statistics.getDislikesCounter())
                .favouritesCounter(statistics.getFavouritesCounter())
                .viewsCounter(statistics.getViewsCounter())
                .build();
    }

}
