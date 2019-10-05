package com.flatrental.domain.announcement.attributes.apartmentamenities;

import com.flatrental.api.SimpleResourceDTO;
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

    public List<ApartmentAmenity> getApartmentAmenities(List<Long> ids) {
        return apartmentAmenityRepository.findAllById(ids);
    }

    public SimpleResourceDTO mapToSimpleResourceDTO(ApartmentAmenity apartmentAmenity) {
        return new SimpleResourceDTO(apartmentAmenity.getId(), apartmentAmenity.getName());
    }

}
