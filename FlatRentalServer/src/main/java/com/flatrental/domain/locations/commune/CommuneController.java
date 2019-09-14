package com.flatrental.domain.locations.commune;

import com.flatrental.api.CommuneDTO;
import com.flatrental.domain.locations.district.District;
import com.flatrental.domain.locations.district.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/commune")
public class CommuneController {

    @Autowired
    private DistrictService districtService;

    @Autowired
    private CommuneService communeService;

    private static final String DISTRICT_ID = "district_id";
    private static final String DISTRICT_ID_PATH = "/{" + DISTRICT_ID + "}";

    @GetMapping(DISTRICT_ID_PATH)
    private List<CommuneDTO> getCommunesForDistrict(@PathVariable(DISTRICT_ID) Long districtId) {
        District district = districtService.getExistingDistrict(districtId);
        return communeService.getCommunesForDistrict(district).stream()
                .map(this::mapToCommuneDTO)
                .collect(Collectors.toList());
    }

    private CommuneDTO mapToCommuneDTO(Commune commune) {
        return new CommuneDTO(commune.getId(), commune.getName(), commune.getType());
    }

}
