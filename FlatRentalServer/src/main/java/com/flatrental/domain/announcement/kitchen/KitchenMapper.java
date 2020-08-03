package com.flatrental.domain.announcement.kitchen;

import com.flatrental.api.KitchenDTO;
import com.flatrental.api.SimpleAttributeDTO;
import com.flatrental.domain.announcement.simpleattribute.SimpleAttributeMapper;
import com.flatrental.domain.announcement.simpleattribute.cookertype.CookerType;
import com.flatrental.domain.announcement.simpleattribute.cookertype.CookerTypeService;
import com.flatrental.domain.announcement.simpleattribute.furnishings.FurnishingItem;
import com.flatrental.domain.announcement.simpleattribute.furnishings.FurnishingService;
import com.flatrental.domain.announcement.simpleattribute.kitchentype.KitchenType;
import com.flatrental.domain.announcement.simpleattribute.kitchentype.KitchenTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class KitchenMapper {

    private final SimpleAttributeMapper simpleAttributeMapper;
    private final FurnishingService furnishingService;
    private final KitchenTypeService kitchenTypeService;
    private final CookerTypeService cookerTypeService;

    public Kitchen mapToKitchen(KitchenDTO kitchenDTO) {
        if (kitchenDTO == null) {
            return null;
        }
        return Kitchen.builder()
                .kitchenArea(kitchenDTO.getKitchenArea())
                .furnishing(mapToFurnishing(kitchenDTO.getFurnishing()))
                .kitchenType(mapToKitchenType(kitchenDTO.getKitchenType()))
                .cookerType(mapToCookerType(kitchenDTO.getCookerType()))
                .build();
    }

    private Set<FurnishingItem> mapToFurnishing(Collection<SimpleAttributeDTO> simpleAttributeDTOs) {
        return simpleAttributeMapper.mapToSimpleAttributes(simpleAttributeDTOs, furnishingService::getFurnishingItems);
    }

    private KitchenType mapToKitchenType(SimpleAttributeDTO kitchenType) {
        return simpleAttributeMapper.mapToSimpleAttribute(kitchenType, kitchenTypeService::getExistingKitchenType);
    }

    private CookerType mapToCookerType(SimpleAttributeDTO cookerType) {
        return simpleAttributeMapper.mapToSimpleAttribute(cookerType, cookerTypeService::getExistingCookerType);
    }

    public KitchenDTO mapToKitchenDTO(Kitchen kitchen) {
        if (kitchen == null) {
            return null;
        }
        return KitchenDTO.builder()
                .kitchenArea(kitchen.getKitchenArea())
                .furnishing(simpleAttributeMapper.mapToSimpleAttributeDTOs(kitchen.getFurnishing()))
                .kitchenType(simpleAttributeMapper.mapToSimpleAttributeDTO(kitchen.getKitchenType()))
                .cookerType(simpleAttributeMapper.mapToSimpleAttributeDTO(kitchen.getCookerType()))
                .build();
    }

}
