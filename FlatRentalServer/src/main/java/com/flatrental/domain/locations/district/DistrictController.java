package com.flatrental.domain.locations.district;

import com.flatrental.api.location.DistrictDTO;
import com.flatrental.domain.locations.voivodeship.Voivodeship;
import com.flatrental.domain.locations.voivodeship.VoivodeshipService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "Administrative units")
@RestController
@RequestMapping("/api/district")
@RequiredArgsConstructor
public class DistrictController {

    private final DistrictService districtService;
    private final VoivodeshipService voivodeshipService;

    private static final String VOIVODESHIP_ID = "voivodeship_id";
    private static final String VOIVODESHIP_PATH = "/{" + VOIVODESHIP_ID + "}";

    @GetMapping(VOIVODESHIP_PATH)
    public List<DistrictDTO> getDistrictsForVoivodeship(@PathVariable(VOIVODESHIP_ID) Long voivodeshipId) {
        Voivodeship voivodeship = voivodeshipService.getExistingVoivodeship(voivodeshipId);
        return districtService.getDistrictsForVoivodeship(voivodeship).stream()
                .map(districtService::mapToDistrictDTO)
                .collect(Collectors.toList());
    }

}
