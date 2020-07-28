package com.flatrental.domain.announcement.simpleattributes.apartmentstate;

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
@RequestMapping("/api/apartmentstate")
@RequiredArgsConstructor
public class ApartmentStateController {

    private final ApartmentStateService apartmentStateService;

    @GetMapping
    public List<SimpleResourceDTO> getApartmentStateTypes() {
        return apartmentStateService.getAllApartmentStateTypes().stream()
                .map(apartmentStateService::mapToSimpleResourceDTO)
                .collect(Collectors.toList());
    }

}
