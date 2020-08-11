package com.flatrental.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class AnnouncementBrowseDTO {

    @NotNull
    private Long id;

    @NotNull
    private String type;

    @NotNull
    @NotBlank
    @Size(max = 60)
    private String title;

    @Positive
    private Integer totalArea;

    @Positive
    @Max(10)
    private Integer numberOfRooms;

    @Positive
    private Integer pricePerMonth;

    @NotNull
    private AddressDTO address;

    @NotNull
    private List<RoomBrowseDTO> rooms;

    @NotNull
    private List<FileDTO> announcementImages;

    @NotNull
    private AnnouncementStatisticsDTO statistics;

    @NotNull
    private ManagedObjectDTO info;

}
