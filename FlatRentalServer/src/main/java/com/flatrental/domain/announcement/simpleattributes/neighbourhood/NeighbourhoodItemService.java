package com.flatrental.domain.announcement.simpleattributes.neighbourhood;

import com.flatrental.api.SimpleResourceDTO;
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

    public List<NeighbourhoodItem> getNeighbourhoodItems(List<Long> ids) {
        return neighbourhoodItemRepository.findAllById(ids);
    }

    public SimpleResourceDTO mapToSimpleResourceDTO(NeighbourhoodItem neighbourhoodItem) {
        return new SimpleResourceDTO(neighbourhoodItem.getId(), neighbourhoodItem.getName());
    }

}
