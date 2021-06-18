package com.flatrental.domain.locations.elasticsearch;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class IndexationStatus {

    private long numberOfObjectsToIndex;
    private long numberOfIndexedObjects;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Status status;
    private String message;

    enum Status {
        SUCCESSFUL,
        IN_PROGRESS,
        SCHEDULED,
        FAILED
    }

}
