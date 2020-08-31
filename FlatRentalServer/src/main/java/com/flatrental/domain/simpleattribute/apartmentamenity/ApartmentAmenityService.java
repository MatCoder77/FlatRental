package com.flatrental.domain.simpleattribute.apartmentamenity;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApartmentAmenityService {

    private final ApartmentAmenityRepository apartmentAmenityRepository;

    public List<ApartmentAmenity> getAllApartmentAmenityTypes() {
        return apartmentAmenityRepository.findAll();
    }

    public List<ApartmentAmenity> getApartmentAmenities(Collection<Long> ids) {
        return apartmentAmenityRepository.findAllById(ids);
    }

}
