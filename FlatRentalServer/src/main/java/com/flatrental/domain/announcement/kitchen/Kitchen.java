package com.flatrental.domain.announcement.kitchen;

import com.flatrental.domain.simpleattribute.cookertype.CookerType;
import com.flatrental.domain.simpleattribute.furnishings.FurnishingItem;
import com.flatrental.domain.simpleattribute.kitchentype.KitchenType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
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

    @ManyToOne(fetch = FetchType.LAZY)
    private KitchenType kitchenType;

    private Integer kitchenArea;

    @ManyToOne(fetch = FetchType.LAZY)
    private CookerType cookerType;

    @ManyToMany
    @Fetch(FetchMode.SUBSELECT)
    @Where(clause = "furnishing_type = 'KITCHEN'")
    @JoinTable(name = "Announcements_X_FurnishingItems")
    private Set<FurnishingItem> furnishing;

}
