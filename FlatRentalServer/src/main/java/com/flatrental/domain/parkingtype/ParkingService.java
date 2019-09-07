package com.flatrental.domain.parkingtype;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkingService {

    @Autowired
    private ParkingTypeRepository parkingTypeRepository;

    public List<ParkingType> getAllParkingTypes() {
        return parkingTypeRepository.findAll();
    }

}
