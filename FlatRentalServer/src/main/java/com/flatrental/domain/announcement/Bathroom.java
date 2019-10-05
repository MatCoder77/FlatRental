package com.flatrental.domain.announcement;

import com.flatrental.domain.announcement.attributes.furnishings.FurnishingItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.Set;

@Embeddable
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Bathroom {

    private Integer numberOfBathrooms;

    private Boolean separateWC;

    @ManyToMany
    @JoinTable(name = "Announcements_X_FurnishingItems")
    private Set<FurnishingItem> furnishing;

}
