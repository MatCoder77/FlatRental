package com.flatrental.domain.locations.localitypart;

import com.flatrental.api.location.LocalityPartDTO;
import com.flatrental.domain.locations.locality.Locality;
import com.flatrental.domain.locations.locality.LocalityService;
import com.flatrental.domain.locations.localitydistrict.LocalityDistrict;
import com.flatrental.domain.locations.localitydistrict.LocalityDistrictService;
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
@RequestMapping("/api/locality-part")
@RequiredArgsConstructor
public class LocalityPartController {

    private final LocalityService localityService;
    private final LocalityDistrictService localityDistrictService;
    private final LocalityPartService localityPartService;

    private static final String ID = "id";
    private static final String LOCALITY_PARTS_FOR_PARENT_LOCALITY_PATH = "/for-parent-locality/{" + ID + "}";
    private static final String LOCALITY_PARTS_FOR_PARENT_LOCALITY_DISTRICT_PATH = "/for-parent-locality-district/{" + ID + "}";


    @GetMapping(LOCALITY_PARTS_FOR_PARENT_LOCALITY_PATH)
    public List<LocalityPartDTO> getLocalityPartsForParentLocality(@PathVariable(ID) Long parentLocalityId) {
        Locality parentLocality = localityService.getExistingLocality(parentLocalityId);
        return localityPartService.getLocalityPartsForParentLocality(parentLocality)
                .stream()
                .map(localityPartService::mapToLocalityPart)
                .collect(Collectors.toList());
    }

    @GetMapping(LOCALITY_PARTS_FOR_PARENT_LOCALITY_DISTRICT_PATH)
    public List<LocalityPartDTO> getLocalityPartsForParentLocalityDistrict(@PathVariable(ID) Long parentLocalityDistrictId) {
        LocalityDistrict localityDistrict = localityDistrictService.getExistingLocalityDistrict(parentLocalityDistrictId);
        return localityPartService.getLocalityPartsForParentLocalityDistrict(localityDistrict)
                .stream()
                .map(localityPartService::mapToLocalityPart)
                .collect(Collectors.toList());
    }

}
