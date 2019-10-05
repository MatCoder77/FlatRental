package com.flatrental.domain.announcement.attributes.heatingtype;

import com.flatrental.api.SimpleResourceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
public class HeatingTypeService {

    @Autowired
    private HeatingTypeRepository heatingTypeRepository;

    private static final String NOT_FOUND = "There is no HeatingType with id {0}";

    public List<HeatingType> getAllHeatingTypes() {
        return heatingTypeRepository.findAll();
    }

    public HeatingType getExistingHeatingType(Long id) {
        return heatingTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(MessageFormat.format(NOT_FOUND, id)));
    }

    public SimpleResourceDTO mapToSimpleResourceDTO(HeatingType heatingType) {
        return new SimpleResourceDTO(heatingType.getId(), heatingType.getName());
    }

}
