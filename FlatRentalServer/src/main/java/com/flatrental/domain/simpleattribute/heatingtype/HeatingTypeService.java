package com.flatrental.domain.simpleattribute.heatingtype;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HeatingTypeService {

    private final HeatingTypeRepository heatingTypeRepository;

    private static final String NOT_FOUND = "There is no HeatingType with id {0}";

    public List<HeatingType> getAllHeatingTypes() {
        return heatingTypeRepository.findAll();
    }

    public HeatingType getExistingHeatingType(Long id) {
        return heatingTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(MessageFormat.format(NOT_FOUND, id)));
    }

}
