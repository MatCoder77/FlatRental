package com.flatrental.domain.announcement.simpleattributes.parkingtype;

import com.flatrental.api.SimpleResourceDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/parkingtype")
public class ParkingTypeController {

    private final ParkingTypeService parkingTypeService;

    @GetMapping
    public List<SimpleResourceDTO> getParkingTypes() {
        return parkingTypeService.getAllParkingTypes().stream()
                .map(parkingTypeService::mapToSimpleResourceDTO)
                .collect(Collectors.toList());
    }

}
