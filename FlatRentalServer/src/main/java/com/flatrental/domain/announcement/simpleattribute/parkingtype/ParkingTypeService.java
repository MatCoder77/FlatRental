package com.flatrental.domain.announcement.simpleattribute.parkingtype;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParkingTypeService {

    private final ParkingTypeRepository parkingTypeRepository;

    private static final String NOT_FOUND = "There is no ParkingType with id {0}";

    public List<ParkingType> getAllParkingTypes() {
        return parkingTypeRepository.findAll();
    }

    public ParkingType getExistingParkingType(Long id) {
        return parkingTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(MessageFormat.format(NOT_FOUND, id)));
    }

}
