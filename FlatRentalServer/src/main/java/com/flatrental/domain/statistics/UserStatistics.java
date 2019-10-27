package com.flatrental.domain.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStatistics {

    @NotNull
    private long opinionsCounter;

    @NotNull
    private long commentsCounter;

    @NotNull
    private double rating;

}
