package com.flatrental.domain.simpleattribute.cookertype;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CookerTypeService {

    private final CookerTypeRepository cookerTypeRepository;

    private static final String NOT_FOUND = "There is no CookerType with id {0}";

    public List<CookerType> getAllCookerTypes() {
        return cookerTypeRepository.findAll();
    }

    public CookerType getExistingCookerType(Long id) {
        return cookerTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(MessageFormat.format(NOT_FOUND, id)));
    }

}
