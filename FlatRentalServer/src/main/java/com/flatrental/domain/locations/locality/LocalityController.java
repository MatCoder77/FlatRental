package com.flatrental.domain.locations.locality;

import com.flatrental.api.LocalityDTO;
import com.flatrental.domain.locations.commune.Commune;
import com.flatrental.domain.locations.commune.CommuneService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/locality")
@RequiredArgsConstructor
public class LocalityController {

    private final LocalityService localityService;
    private final CommuneService communeService;

    private static final String COMMUNE_ID = "commune_id";
    private static final String COMMUNE_ID_PATH = "/{" + COMMUNE_ID + "}";

    @GetMapping(COMMUNE_ID_PATH)
    public List<LocalityDTO> getLocalitiesForCommune(@PathVariable(COMMUNE_ID) Long communeId) {
        Commune commune = communeService.getExistingCommune(communeId);
        return localityService.getLocalitiesForCommune(commune).stream()
                .map(localityService::mapToLocalityDTO)
                .collect(Collectors.toList());
    }

}
