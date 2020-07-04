package com.flatrental.domain.announcement.simpleattributes.furnishings;

import com.flatrental.api.SimpleResourceDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FurnishingService {

    private final FurnishingRepository furnishingRepository;

    public List<FurnishingItem> getFurnishingItems() {
        return furnishingRepository.findAll();
    }

    public List<FurnishingItem> getFurnishingItems(List<Long> ids) {
        return furnishingRepository.findAllById(ids);
    }

    public SimpleResourceDTO mapToSimpleResourceDTO(FurnishingItem furnishingItem) {
        return new SimpleResourceDTO(furnishingItem.getId(), furnishingItem.getName());
    }

}
