package com.flatrental.domain.announcement.attributes.cookertype;

import com.flatrental.api.SimpleResourceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
public class CookerTypeService {

    @Autowired
    private CookerTypeRepository cookerTypeRepository;

    private static final String NOT_FOUND = "There is no CookerType with id {0}";

    public List<CookerType> getAllCookerTypes() {
        return cookerTypeRepository.findAll();
    }

    public CookerType getExistingCookerType(Long id) {
        return cookerTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(MessageFormat.format(NOT_FOUND, id)));
    }

    public SimpleResourceDTO mapToSimpleResourceDTO(CookerType cookerType) {
        return new SimpleResourceDTO(cookerType.getId(), cookerType.getName());
    }

}
