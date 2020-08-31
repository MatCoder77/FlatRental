package com.flatrental.domain.announcement.bathroom;

import com.flatrental.domain.simpleattribute.furnishings.FurnishingItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

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
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(name = "Announcements_X_FurnishingItems")
    @Where(clause = "furnishing_type = 'BATHROOM'")
    private Set<FurnishingItem> furnishing;

}
