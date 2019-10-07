package com.flatrental.domain.announcement;

import com.flatrental.domain.announcement.simpleattributes.cookertype.CookerType;
import com.flatrental.domain.announcement.simpleattributes.furnishings.FurnishingItem;
import com.flatrental.domain.announcement.simpleattributes.kitchentype.KitchenType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.Embeddable;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.Set;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Kitchen {

    @ManyToOne
    private KitchenType kitchenType;

    private Integer kitchenArea;

    @ManyToOne
    private CookerType cookerType;

    @ManyToMany
    @Where(clause = "furnishing_type = 'KITCHEN'")
    @JoinTable(name = "Announcements_X_FurnishingItems")
    private Set<FurnishingItem> furnishing;

}
