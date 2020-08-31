package com.flatrental.domain.simpleattribute.furnishings;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FurnishingService {

    private final FurnishingRepository furnishingRepository;

    public List<FurnishingItem> getFurnishingItemsWithType(FurnishingType furnishingType) {
        return furnishingRepository.findAll().stream()
                .filter(furnishingItem -> furnishingType == furnishingItem.getFurnishingType())
                .collect(Collectors.toList());
    }

    public List<FurnishingItem> getFurnishingItems() {
        return furnishingRepository.findAll();
    }

    public List<FurnishingItem> getFurnishingItems(Collection<Long> ids) {
        return furnishingRepository.findAllById(ids);
    }

}
