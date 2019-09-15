package com.flatrental.domain.announcement.attributes.neighbourhood;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NeighbourhoodItemService {

    @Autowired
    private NeighbourhoodItemRepository neighbourhoodItemRepository;

    public List<NeighbourhoodItem> getAllNeighbourItems() {
        return neighbourhoodItemRepository.findAll();
    }

}
