package com.flatrental.domain.apartmentamenities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApartmentAmenityService {

    @Autowired
    private ApartmentAmenityRepository apartmentAmenityRepository;

    public List<ApartmentAmenity> getAllApartmentAmenityTypes() {
        return apartmentAmenityRepository.findAll();
    }

}
