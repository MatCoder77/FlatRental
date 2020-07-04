package com.flatrental.domain.locations.commune;

import com.flatrental.api.CommuneDTO;
import com.flatrental.domain.locations.district.District;
import com.flatrental.domain.locations.district.DistrictService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/commune")
@RequiredArgsConstructor
public class CommuneController {

    private final DistrictService districtService;
    private final CommuneService communeService;

    private static final String DISTRICT_ID = "district_id";
    private static final String DISTRICT_ID_PATH = "/{" + DISTRICT_ID + "}";

    @GetMapping(DISTRICT_ID_PATH)
    public List<CommuneDTO> getCommunesForDistrict(@PathVariable(DISTRICT_ID) Long districtId) {
        District district = districtService.getExistingDistrict(districtId);
        return communeService.getCommunesForDistrict(district).stream()
                .map(communeService::mapToCommuneDTO)
                .collect(Collectors.toList());
    }

}
