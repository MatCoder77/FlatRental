package com.flatrental.domain.announcement.simpleattributes.neighbourhood;

import com.flatrental.api.SimpleResourceDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NeighbourhoodItemService {

    private final NeighbourhoodItemRepository neighbourhoodItemRepository;

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
