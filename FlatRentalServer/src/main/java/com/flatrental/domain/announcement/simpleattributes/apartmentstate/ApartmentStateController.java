package com.flatrental.domain.announcement.simpleattributes.apartmentstate;

import com.flatrental.api.SimpleResourceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/apartmentstate")
public class ApartmentStateController {

    @Autowired
    private ApartmentStateService apartmentStateService;

    @GetMapping
    public List<SimpleResourceDTO> getApartmentStateTypes() {
        return apartmentStateService.getAllApartmentStateTypes().stream()
                .map(apartmentStateService::mapToSimpleResourceDTO)
                .collect(Collectors.toList());
    }

}
