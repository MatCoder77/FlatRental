package com.flatrental.domain.locations.district;

import com.flatrental.api.DistrictDTO;
import com.flatrental.domain.locations.voivodeship.Voivodeship;
import com.flatrental.domain.locations.voivodeship.VoivodeshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/district")
public class DistrictController {

    @Autowired
    private DistrictService districtService;

    @Autowired
    private VoivodeshipService voivodeshipService;

    private static final String VOIVODESHIP_ID = "voivodeship_id";
    private static final String VOIVODESHIP_PATH = "/{" + VOIVODESHIP_ID + "}";

    @GetMapping(VOIVODESHIP_PATH)
    public List<DistrictDTO> getDistrictsForVoivodeship(@PathVariable(VOIVODESHIP_ID) Long voivodeshipId) {
        Voivodeship voivodeship = voivodeshipService.getExistingVoivodeship(voivodeshipId);
        return districtService.getDistrictsForVoivodeship(voivodeship).stream()
                .map(this::mapToDistricDTO)
                .collect(Collectors.toList());
    }

    private DistrictDTO mapToDistricDTO(District district) {
        return new DistrictDTO(district.getId(), district.getName(), district.getType());
    }

}
