package com.flatrental.domain.announcement.simpleattributes.kitchentype;

import com.flatrental.api.SimpleResourceDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KitchenTypeService {

    private final KitchenTypeRepository kitchenTypeRepository;

    private static final String NOT_FOUND = "There is no KitchenType with id {0}";

    public List<KitchenType> getAllKitchenTypes() {
        return kitchenTypeRepository.findAll();
    }

    public KitchenType getExistingKitchenType(Long id) {
        return kitchenTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(MessageFormat.format(NOT_FOUND, id)));
    }

    public SimpleResourceDTO mapToSimpleResourceDTO(KitchenType kitchenType) {
        return new SimpleResourceDTO(kitchenType.getId(), kitchenType.getName());
    }

}
