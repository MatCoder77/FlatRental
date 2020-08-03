package com.flatrental.domain.statistics.announcement;

import com.flatrental.api.AnnouncementStatisticsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnnouncementsStatisticsMapper {

    public AnnouncementStatisticsDTO mapToAnnouncementStatisticsDTO(AnnouncementStatistics announcementStatistics) {
        if (announcementStatistics == null) {
            return null;
        }
        return AnnouncementStatisticsDTO.builder()
                .commentsCounter(announcementStatistics.getCommentsCounter())
                .likesCounter(announcementStatistics.getLikesCounter())
                .dislikesCounter(announcementStatistics.getDislikesCounter())
                .favouritesCounter(announcementStatistics.getFavouritesCounter())
                .viewsCounter(announcementStatistics.getViewsCounter())
                .build();
    }

    public AnnouncementStatistics mapToAnnouncementStatistics(AnnouncementStatisticsDTO announcementStatisticsDTO) {
        if (announcementStatisticsDTO == null) {
            return null;
        }
        return AnnouncementStatistics.builder()
                .likesCounter(announcementStatisticsDTO.getLikesCounter())
                .dislikesCounter(announcementStatisticsDTO.getDislikesCounter())
                .commentsCounter(announcementStatisticsDTO.getCommentsCounter())
                .favouritesCounter(announcementStatisticsDTO.getFavouritesCounter())
                .viewsCounter(announcementStatisticsDTO.getViewsCounter())
                .build();
    }

}
