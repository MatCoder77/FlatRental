package com.flatrental.domain.locations.street;


import com.flatrental.api.StreetDTO;
import com.flatrental.domain.locations.abstractlocality.AbstractLocality;
import com.flatrental.domain.locations.abstractlocality.AbstractLocalityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/street")
@RequiredArgsConstructor
public class StreetController {

    private final AbstractLocalityService abstractLocalityService;
    private final StreetService streetService;

    private static final String ID = "id";
    private static final String STREETS_FOR_PARENT_LOCALITY_PATH = "/for-parent-locality/{" + ID + "}";

    @GetMapping(STREETS_FOR_PARENT_LOCALITY_PATH)
    public List<StreetDTO> getStreetsForParentLocality(@PathVariable(ID) Long parentLocationId) {
        AbstractLocality parentLocality = abstractLocalityService.getExistingAbstratcLocality(parentLocationId);
        return parentLocality.getStreets().stream()
                .map(streetService::mapToStreetDTO)
                .collect(Collectors.toList());
    }

}
