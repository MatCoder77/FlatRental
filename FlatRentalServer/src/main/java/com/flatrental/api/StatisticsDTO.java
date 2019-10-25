package com.flatrental.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatisticsDTO {

    long likesCounter;
    long dislikesCounter;
    long commentsCounter;
    long viewsCounter;
    long favouritesCounter;

}
