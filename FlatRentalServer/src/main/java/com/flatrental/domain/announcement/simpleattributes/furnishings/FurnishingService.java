package com.flatrental.domain.announcement.simpleattributes.furnishings;

import com.flatrental.api.SimpleResourceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FurnishingService {

    @Autowired
    private FurnishingRepository furnishingRepository;

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
