package com.flatrental.domain.locations.localitydistrict;

import com.flatrental.api.LocalityDistrictDTO;
import com.flatrental.domain.locations.locality.Locality;
import com.flatrental.domain.locations.locality.LocalityService;
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
@RequestMapping("/api/localitydistrict")
@RequiredArgsConstructor
public class LocalityDistrictController {

    private final LocalityService localityService;
    private final LocalityDistrictService localityDistrictService;

    private static final String LOCALITY_ID = "locality_id";
    private static final String LOCALITY_ID_PATH = "/{" + LOCALITY_ID + "}";

    @GetMapping(LOCALITY_ID_PATH)
    public List<LocalityDistrictDTO> getLocalityDistrictsForLocality(@PathVariable(LOCALITY_ID) Long localityId) {
        Locality locality = localityService.getExistingLocality(localityId);
        return localityDistrictService.getAllLocalityDistrictsForLocality(locality).stream()
                .map(localityDistrictService::mapToLocalityDistrictDTO)
                .collect(Collectors.toList());
    }

}
