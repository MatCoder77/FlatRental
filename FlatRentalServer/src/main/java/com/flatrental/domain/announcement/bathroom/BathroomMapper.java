package com.flatrental.domain.announcement.bathroom;

import com.flatrental.api.announcement.BathroomDTO;
import com.flatrental.api.simpleattribute.SimpleAttributeDTO;
import com.flatrental.domain.simpleattribute.SimpleAttributeMapper;
import com.flatrental.domain.simpleattribute.furnishings.FurnishingItem;
import com.flatrental.domain.simpleattribute.furnishings.FurnishingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BathroomMapper {

    private final SimpleAttributeMapper simpleAttributeMapper;
    private final FurnishingService furnishingService;

    public Bathroom mapToBathroom(BathroomDTO bathroomDTO) {
        if (bathroomDTO == null) {
            return null;
        }
        return Bathroom.builder()
                .numberOfBathrooms(bathroomDTO.getNumberOfBathrooms())
                .separateWC(bathroomDTO.getSeparateWC())
                .furnishing(getFurnishing(bathroomDTO.getFurnishing()))
                .build();
    }

    private Set<FurnishingItem> getFurnishing(List<SimpleAttributeDTO> furnishingDTOs) {
        return simpleAttributeMapper.mapToSimpleAttributes(furnishingDTOs, furnishingService::getFurnishingItems);
    }

    public BathroomDTO mapToBathroomDTO(Bathroom bathroom) {
        if (bathroom == null) {
            return null;
        }
        return BathroomDTO.builder()
                .numberOfBathrooms(bathroom.getNumberOfBathrooms())
                .separateWC(bathroom.getSeparateWC())
                .furnishing(simpleAttributeMapper.mapToSimpleAttributeDTOs(bathroom.getFurnishing()))
                .build();
    }

}
