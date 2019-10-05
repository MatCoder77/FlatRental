package com.flatrental.domain.announcement.attributes.apartmentstate;

import com.flatrental.api.SimpleResourceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
public class ApartmentStateService {

    @Autowired
    private ApartmentStateRepository apartmentStateRepository;

    private static final String NOT_FOUND = "There is no ApartmentState with id {0}";

    public List<ApartmentState> getAllApartmentStateTypes() {
        return apartmentStateRepository.findAll();
    }

    public ApartmentState getExistingApartmentState(Long id) {
        return apartmentStateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(MessageFormat.format(NOT_FOUND, id)));
    }

    public SimpleResourceDTO mapToSimpleResourceDTO(ApartmentState apartmentState) {
        return new SimpleResourceDTO(apartmentState.getId(), apartmentState.getName());
    }

}
