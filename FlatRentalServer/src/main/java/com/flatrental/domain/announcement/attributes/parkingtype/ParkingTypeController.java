package com.flatrental.domain.announcement.attributes.parkingtype;

import com.flatrental.api.SimpleResourceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/parkingtype")
public class ParkingTypeController {

    @Autowired
    private ParkingTypeService parkingTypeService;

    @GetMapping
    public List<SimpleResourceDTO> getParkingTypes() {
        return parkingTypeService.getAllParkingTypes().stream()
                .map(this::mapToSimpleResourceDTO)
                .collect(Collectors.toList());
    }

    private SimpleResourceDTO mapToSimpleResourceDTO(ParkingType parkingType) {
        return new SimpleResourceDTO(parkingType.getId(), parkingType.getName());
    }

}
