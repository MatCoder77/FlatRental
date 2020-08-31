package com.flatrental.domain.announcement.room;

import com.flatrental.domain.simpleattribute.furnishings.FurnishingItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Set;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Rooms")
public class Room {

    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    private Long id;

    @Positive
    private Integer numberOfPersons;

    @PositiveOrZero
    private Integer personsOccupied;

    @Positive
    private Integer area;

    @Positive
    private Integer pricePerMonth;

    @ManyToMany
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(name = "Rooms_X_FurnishingItems")
    private Set<FurnishingItem> furnishings;

    @NotNull
    @Positive
    private Integer roomNumber;

}
