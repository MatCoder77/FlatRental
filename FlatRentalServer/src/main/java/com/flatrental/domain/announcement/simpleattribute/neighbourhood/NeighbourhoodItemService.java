package com.flatrental.domain.announcement.simpleattribute.neighbourhood;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NeighbourhoodItemService {

    private final NeighbourhoodItemRepository neighbourhoodItemRepository;

    public List<NeighbourhoodItem> getAllNeighbourItems() {
        return neighbourhoodItemRepository.findAll();
    }

    public List<NeighbourhoodItem> getNeighbourhoodItems(Collection<Long> ids) {
        return neighbourhoodItemRepository.findAllById(ids);
    }

}
