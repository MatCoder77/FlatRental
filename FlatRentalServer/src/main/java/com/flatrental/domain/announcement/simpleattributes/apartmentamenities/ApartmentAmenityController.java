package com.flatrental.domain.announcement.simpleattributes.apartmentamenities;

import com.flatrental.api.SimpleResourceDTO;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "Reference Attributes")
@RestController
@RequestMapping("/api/apartmentamenity")
@RequiredArgsConstructor
public class ApartmentAmenityController {

    private final ApartmentAmenityService apartmentAmenityService;

    @GetMapping
    public List<SimpleResourceDTO> getAllApartmentAmenityTypes() {
        return apartmentAmenityService.getAllApartmentAmenityTypes().stream()
                .map(apartmentAmenityService::mapToSimpleResourceDTO)
                .collect(Collectors.toList());
    }

}
