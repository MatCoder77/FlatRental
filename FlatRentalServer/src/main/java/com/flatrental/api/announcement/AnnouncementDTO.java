package com.flatrental.api.announcement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.flatrental.api.file.FileDTO;
import com.flatrental.api.managedobject.ManagedObjectDTO;
import com.flatrental.api.simpleattribute.SimpleAttributeDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class AnnouncementDTO {

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

    @PositiveOrZero
    private Integer additionalCostsPerMonth;

    @Positive
    private Integer securityDeposit;

    @Min(-1)
    private Integer floor;

    @PositiveOrZero
    private Integer maxFloorInBuilding;

    @JsonSerialize(as = Date.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date availableFrom;

    @NotNull
    private AddressDTO address;

    private SimpleAttributeDTO buildingType;
    private SimpleAttributeDTO buildingMaterial;
    private SimpleAttributeDTO heatingType;
    private SimpleAttributeDTO windowType;
    private SimpleAttributeDTO parkingType;
    private SimpleAttributeDTO apartmentState;

    @Positive
    private Integer yearBuilt;

    private Boolean wellPlanned;
    private List<SimpleAttributeDTO> apartmentAmenities;
    private List<RoomDTO> rooms;
    private KitchenDTO kitchen;
    private BathroomDTO bathroom;
    private List<SimpleAttributeDTO> preferences;
    private List<SimpleAttributeDTO> neighbourhood;

    @Size(max = 10000)
    private String description;

    private List<FileDTO> announcementImages;
    private String aboutFlatmates;

    @PositiveOrZero
    private Integer numberOfFlatmates;

    private ManagedObjectDTO info;
    private AnnouncementStatisticsDTO statistics;
    private Long quality;

}
