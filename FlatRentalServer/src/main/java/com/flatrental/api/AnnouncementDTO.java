package com.flatrental.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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

    SimpleResourceDTO buildingType;
    SimpleResourceDTO buildingMaterial;
    SimpleResourceDTO heatingType;
    SimpleResourceDTO windowType;
    SimpleResourceDTO parkingType;
    SimpleResourceDTO apartmentState;

    @Positive
    private Integer yearBuilt;

    private Boolean wellPlanned;
    List<SimpleResourceDTO> apartmentAmenities;
    List<RoomDTO> rooms;
    KitchenDTO kitchen;
    BathroomDTO bathroom;
    List<SimpleResourceDTO> preferences;
    List<SimpleResourceDTO> neighbourhood;

    @Size(max = 10000)
    private String description;

    List<FileDTO> announcementImages;
    String aboutFlatmates;

    @PositiveOrZero
    Integer numberOfFlatmates;

    ManagedObjectDTO info;

}
