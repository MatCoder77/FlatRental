package com.flatrental.domain.announcement.simpleattributes.apartmentamenities;

import com.flatrental.api.SimpleResourceDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApartmentAmenityService {

    private final ApartmentAmenityRepository apartmentAmenityRepository;

    public List<ApartmentAmenity> getAllApartmentAmenityTypes() {
        return apartmentAmenityRepository.findAll();
    }

    public List<ApartmentAmenity> getApartmentAmenities(List<Long> ids) {
        return apartmentAmenityRepository.findAllById(ids);
    }

    public SimpleResourceDTO mapToSimpleResourceDTO(ApartmentAmenity apartmentAmenity) {
        return new SimpleResourceDTO(apartmentAmenity.getId(), apartmentAmenity.getName());
    }

}
