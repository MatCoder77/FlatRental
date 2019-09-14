package com.flatrental.domain.locations.locality;

import com.flatrental.api.LocalityDTO;
import com.flatrental.domain.locations.commune.Commune;
import com.flatrental.domain.locations.commune.CommuneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/locality")
public class LocalityController {

    @Autowired
    private LocalityService localityService;

    @Autowired
    private CommuneService communeService;

    private static final String COMMUNE_ID = "commune_id";
    private static final String COMMUNE_ID_PATH = "/{" + COMMUNE_ID + "}";

    @GetMapping(COMMUNE_ID_PATH)
    public List<LocalityDTO> getLocalitiesForCommune(@PathVariable(COMMUNE_ID) Long communeId) {
        Commune commune = communeService.getExistingCommune(communeId);
        return localityService.getLocalitiesForCommune(commune).stream()
                .map(this::mapToLocalityDTO)
                .collect(Collectors.toList());
    }

    private LocalityDTO mapToLocalityDTO(Locality locality) {
        return new LocalityDTO(locality.getId(), locality.getName(), locality.getLocalityType());
    }

}
