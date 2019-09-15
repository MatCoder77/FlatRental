package com.flatrental.domain.announcement.attributes.furnishings;

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

}
