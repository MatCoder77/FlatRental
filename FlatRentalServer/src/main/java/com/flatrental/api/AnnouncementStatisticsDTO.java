package com.flatrental.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnnouncementStatisticsDTO {

    private long likesCounter;
    private long dislikesCounter;
    private long commentsCounter;
    private long viewsCounter;
    private long favouritesCounter;

}
