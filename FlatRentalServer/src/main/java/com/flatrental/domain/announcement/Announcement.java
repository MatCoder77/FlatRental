package com.flatrental.domain.announcement;

import com.flatrental.domain.managedobject.ManagedObject;
import com.flatrental.domain.announcement.address.Address;
import com.flatrental.domain.announcement.simpleattributes.preferences.Preference;
import com.flatrental.domain.announcement.simpleattributes.apartmentamenities.ApartmentAmenity;
import com.flatrental.domain.announcement.simpleattributes.apartmentstate.ApartmentState;
import com.flatrental.domain.announcement.simpleattributes.buildingmaterial.BuildingMaterial;
import com.flatrental.domain.announcement.simpleattributes.buildingtype.BuildingType;
import com.flatrental.domain.announcement.simpleattributes.heatingtype.HeatingType;
import com.flatrental.domain.announcement.simpleattributes.neighbourhood.NeighbourhoodItem;
import com.flatrental.domain.file.File;
import com.flatrental.domain.announcement.simpleattributes.parkingtype.ParkingType;
import com.flatrental.domain.announcement.simpleattributes.windowtype.WindowType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
@SuperBuilder(toBuilder = true)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Announcements")
public class Announcement extends ManagedObject {

    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AnnouncementType type;

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

    @ManyToMany
    @JoinTable(name = "Announcements_X_ApartmentAmenities")
    private Set<ApartmentAmenity> apartmentAmenities;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "announcement_id")
    private Set<Room> rooms;

    private Kitchen kitchen;

    private Bathroom bathroom;

    @ManyToMany
    @JoinTable(name = "Announcements_X_Preferences")
    private Set<Preference> preferences;

    @ManyToMany
    @JoinTable(name = "Announcements_X_NeighbourhoodItems")
    private Set<NeighbourhoodItem> neighbourhood;

    @Column(length = 1000)
    private String description;

    @ElementCollection
    @CollectionTable(name = "Announcements_X_Images")
    @MapKeyColumn(name = "IMAGE_NUMBER")
    private Map<Integer, File> announcementImages = new HashMap<>();

    private AnnouncementStatistics announcementStatistics;

    private String aboutRoommates;

    @PositiveOrZero
    private Integer numberOfFlatmates;
}
