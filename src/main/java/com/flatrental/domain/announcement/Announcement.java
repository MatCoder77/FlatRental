package com.flatrental.domain.announcement;

import com.flatrental.domain.EntityInfo;
import com.flatrental.domain.antiburglaryprotecions.AntiBurglaryProtecions;
import com.flatrental.domain.buildingmaterial.BuildingMaterial;
import com.flatrental.domain.buildingtype.BuildingType;
import com.flatrental.domain.facilitiesinarea.FacilitiesInArea;
import com.flatrental.domain.heatingtype.HeatingType;
import com.flatrental.domain.images.Image;
import com.flatrental.domain.parkingtype.ParkingType;
import com.flatrental.domain.user.User;
import com.flatrental.domain.windowtype.WindowType;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Entity
public class Announcement extends EntityInfo {

    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AnnouncementType announcementType;

    @NotNull
    @NotBlank
    @Size(max = 200)
    private String title;

    @ManyToOne
    @NotNull
    private User author;

    @ManyToOne
    private BuildingType buildingType;

    @Positive
    private Double totalArea;

    @Positive
    private Integer numberOfRooms;

    @Positive
    private Integer pricePerMonth;

    @Positive
    private Integer securityDeposit;

    @Min(-1)
    private Integer floor;

    @Positive
    private Integer maxFloorInBuilding;

    @FutureOrPresent
    @Temporal(TemporalType.DATE)
    private Date availableFrom;

    private Image mainPhoto;

    private Address address;

    private Integer yearBuilt;

    @ManyToOne
    private BuildingMaterial buildingMaterial;

    @ManyToOne
    private HeatingType heatingType;

    @ManyToOne
    private WindowType windowType;

    @ManyToOne
    private ParkingType parkingType;

    @Enumerated(EnumType.STRING)
    private AppartmentState appartmentState;

    @Column(length = 1000)
    private String description;

    private Kitchen kitchen;

    private Bathroom bathroom;

    @OneToMany
    private Set<Room> rooms;

    @ElementCollection
    @CollectionTable(name = "ANNOUNCEMENT_IMAGES")
    @MapKeyColumn(name = "IMAGE_NUMBER")
    private Map<Integer, Image> announcementImages = new HashMap<>();

    @OneToMany
    private Set<AntiBurglaryProtecions> antiBurglaryProtecions;

    @OneToMany
    private Set<FacilitiesInArea> facilitiesInArea;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AnnouncementState announcementState;

    private AnnouncementStatistics announcementStatistics;

    private String aboutRoommates;
}