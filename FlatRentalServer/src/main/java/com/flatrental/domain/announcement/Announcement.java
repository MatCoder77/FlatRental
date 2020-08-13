package com.flatrental.domain.announcement;

import com.flatrental.domain.announcement.bathroom.Bathroom;
import com.flatrental.domain.announcement.kitchen.Kitchen;
import com.flatrental.domain.announcement.room.Room;
import com.flatrental.domain.managedobject.ManagedObject;
import com.flatrental.domain.announcement.address.Address;
import com.flatrental.domain.announcement.simpleattribute.preferences.Preference;
import com.flatrental.domain.announcement.simpleattribute.apartmentamenity.ApartmentAmenity;
import com.flatrental.domain.announcement.simpleattribute.apartmentstate.ApartmentState;
import com.flatrental.domain.announcement.simpleattribute.buildingmaterial.BuildingMaterial;
import com.flatrental.domain.announcement.simpleattribute.buildingtype.BuildingType;
import com.flatrental.domain.announcement.simpleattribute.heatingtype.HeatingType;
import com.flatrental.domain.announcement.simpleattribute.neighbourhood.NeighbourhoodItem;
import com.flatrental.domain.file.File;
import com.flatrental.domain.announcement.simpleattribute.parkingtype.ParkingType;
import com.flatrental.domain.announcement.simpleattribute.windowtype.WindowType;
import com.flatrental.domain.managedobject.ManagedObject_;
import com.flatrental.domain.statistics.announcement.AnnouncementStatistics;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
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
import java.util.Objects;
import java.util.Set;

@Entity
@SuperBuilder(toBuilder = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Announcements")
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = Announcement.MANY_TO_ONE_ASSOCIATIONS_GRAPH,
                attributeNodes = {
                        @NamedAttributeNode(Announcement_.BUILDING_TYPE),
                        @NamedAttributeNode(Announcement_.BUILDING_MATERIAL),
                        @NamedAttributeNode(Announcement_.HEATING_TYPE),
                        @NamedAttributeNode(Announcement_.WINDOW_TYPE),
                        @NamedAttributeNode(Announcement_.PARKING_TYPE),
                        @NamedAttributeNode(Announcement_.APARTMENT_STATE),
                        @NamedAttributeNode(Announcement_.BUILDING_MATERIAL),
                        @NamedAttributeNode(ManagedObject_.CREATED_BY),
                        @NamedAttributeNode(ManagedObject_.UPDATED_BY)
                }
        )
})
public class Announcement extends ManagedObject {

    public static final String MANY_TO_ONE_ASSOCIATIONS_GRAPH = "Announcement.manyToOneAssociationsGraph";

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

    @ManyToOne(fetch = FetchType.LAZY)
    private BuildingType buildingType;

    @ManyToOne(fetch = FetchType.LAZY)
    private BuildingMaterial buildingMaterial;

    @ManyToOne(fetch = FetchType.LAZY)
    private HeatingType heatingType;

    @ManyToOne(fetch = FetchType.LAZY)
    private WindowType windowType;

    @ManyToOne(fetch = FetchType.LAZY)
    private ParkingType parkingType;

    @ManyToOne(fetch = FetchType.LAZY)
    private ApartmentState apartmentState;

    private Integer yearBuilt;

    private Boolean wellPlanned;

    @ManyToMany
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(name = "Announcements_X_ApartmentAmenities")
    private Set<ApartmentAmenity> apartmentAmenities;

    @OneToMany(cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    @JoinColumn(name = "announcement_id")
    private Set<Room> rooms;

    private Kitchen kitchen;

    private Bathroom bathroom;

    @ManyToMany
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(name = "Announcements_X_Preferences")
    private Set<Preference> preferences;

    @ManyToMany
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(name = "Announcements_X_NeighbourhoodItems")
    private Set<NeighbourhoodItem> neighbourhood;

    @Column(length = 10000)
    private String description;

    @ElementCollection
    @Fetch(FetchMode.SUBSELECT)
    @CollectionTable(name = "Announcements_X_Images")
    @MapKeyColumn(name = "IMAGE_NUMBER")
    private Map<Integer, File> announcementImages = new HashMap<>();

    private String aboutRoommates;

    @PositiveOrZero
    private Integer numberOfFlatmates;

    private AnnouncementStatistics statistics;

    private long quality;

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Announcement)) {
            return false;
        }

        Announcement otherAnnouncement = (Announcement) obj;
        return Objects.equals(id, otherAnnouncement.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
