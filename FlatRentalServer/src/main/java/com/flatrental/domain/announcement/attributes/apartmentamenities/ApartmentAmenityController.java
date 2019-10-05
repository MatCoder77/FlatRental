package com.flatrental.domain.announcement.attributes.apartmentamenities;

import com.flatrental.api.SimpleResourceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/apartmentamenity")
public class ApartmentAmenityController {

    @Autowired
    private ApartmentAmenityService apartmentAmenityService;

    @GetMapping
    public List<SimpleResourceDTO> getAllApartmentAmenityTypes() {
        return apartmentAmenityService.getAllApartmentAmenityTypes().stream()
                .map(apartmentAmenityService::mapToSimpleResourceDTO)
                .collect(Collectors.toList());
    }

}
