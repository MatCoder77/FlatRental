package com.flatrental.domain.announcement;

import com.flatrental.domain.EntityInfo;
import com.flatrental.domain.announcement.attributes.Preference;
import com.flatrental.domain.announcement.attributes.antiburglaryprotecions.AntiBurglaryProtecions;
import com.flatrental.domain.announcement.attributes.apartmentamenities.ApartmentAmenity;
import com.flatrental.domain.announcement.attributes.apartmentstate.ApartmentState;
import com.flatrental.domain.announcement.attributes.buildingmaterial.BuildingMaterial;
import com.flatrental.domain.announcement.attributes.buildingtype.BuildingType;
import com.flatrental.domain.announcement.attributes.heatingtype.HeatingType;
import com.flatrental.domain.announcement.attributes.neighbourhood.NeighbourhoodItem;
import com.flatrental.domain.images.File;
import com.flatrental.domain.announcement.attributes.parkingtype.ParkingType;
import com.flatrental.domain.user.User;
import com.flatrental.domain.announcement.attributes.windowtype.WindowType;

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
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
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

    @ManyToOne
    @NotNull
    private User author;

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

    @Temporal(TemporalType.DATE)
    private Date availableFrom;

    @NotNull
    private Address address;

    @ManyToOne
    private BuildingType buildingType;

    @ManyToOne
    private BuildingMaterial buildingMaterial;

    @ManyToOne
    private HeatingType heatingType;

    @ManyToOne
    private WindowType windowType;

    @ManyToOne
    private ParkingType parkingType;

    @ManyToOne
    private ApartmentState apartmentState;

    private Integer yearBuilt;

    private Boolean wellPlanned;

    @OneToMany
    private Set<ApartmentAmenity> apartmentAmenities;

    @OneToMany
    private Set<Room> rooms;

    private Kitchen kitchen;

    private Bathroom bathroom;

    @OneToMany
    private Set<Preference> preferences;

    @OneToMany
    private Set<NeighbourhoodItem> neighbourhood;

    @Column(length = 1000)
    private String description;

    @ElementCollection
    @CollectionTable(name = "ANNOUNCEMENT_IMAGES")
    @MapKeyColumn(name = "IMAGE_NUMBER")
    private Map<Integer, File> announcementImages = new HashMap<>();




    private File mainPhoto;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AnnouncementState announcementState;

    private AnnouncementStatistics announcementStatistics;



    private String aboutRoommates;
}
