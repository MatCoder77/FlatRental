package com.flatrental.domain.announcement.simpleattributes.parkingtype;

import com.flatrental.api.SimpleResourceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
public class ParkingTypeService {

    @Autowired
    private ParkingTypeRepository parkingTypeRepository;

    private static final String NOT_FOUND = "There is no ParkingType with id {0}";

    public List<ParkingType> getAllParkingTypes() {
        return parkingTypeRepository.findAll();
    }

    public ParkingType getExistingParkingType(Long id) {
        return parkingTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(MessageFormat.format(NOT_FOUND, id)));
    }

    public SimpleResourceDTO mapToSimpleResourceDTO(ParkingType parkingType) {
        return new SimpleResourceDTO(parkingType.getId(), parkingType.getName());
    }

}
