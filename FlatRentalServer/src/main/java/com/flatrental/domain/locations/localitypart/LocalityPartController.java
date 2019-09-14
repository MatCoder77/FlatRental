package com.flatrental.domain.locations.localitypart;

import com.flatrental.api.LocalityPartDTO;
import com.flatrental.domain.locations.abstractlocality.AbstractLocality;
import com.flatrental.domain.locations.abstractlocality.AbstractLocalityService;
import com.flatrental.domain.locations.locality.Locality;
import com.flatrental.domain.locations.locality.LocalityService;
import com.flatrental.domain.locations.localitydistrict.LocalityDistrict;
import com.flatrental.domain.locations.localitydistrict.LocalityDistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/locality-part")
public class LocalityPartController {

    @Autowired
    private LocalityService localityService;

    @Autowired
    private LocalityDistrictService localityDistrictService;

    @Autowired
    private LocalityPartService localityPartService;

    private static final String ID = "id";
    private static final String LOCALITY_PARTS_FOR_PARENT_LOCALITY_PATH = "/for-parent-locality/{" + ID + "}";
    private static final String LOCALITY_PARTS_FOR_PARENT_LOCALITY_DISTRICT_PATH = "/for-parent-locality-district/{" + ID + "}";


    @GetMapping(LOCALITY_PARTS_FOR_PARENT_LOCALITY_PATH)
    public List<LocalityPartDTO> getLocalityPartsForParentLocality(@PathVariable(ID) Long parentLocalityId) {
        Locality parentLocality = localityService.getExistingLocality(parentLocalityId);
        return localityPartService.getLocalityPartsForParentLocality(parentLocality)
                .stream()
                .map(this::mapToLocalityPart)
                .collect(Collectors.toList());
    }

    @GetMapping(LOCALITY_PARTS_FOR_PARENT_LOCALITY_DISTRICT_PATH)
    public List<LocalityPartDTO> getLocalityPartsForParentLocalityDistrict(@PathVariable(ID) Long parentLocalityDistrictId) {
        LocalityDistrict localityDistrict = localityDistrictService.getExistingLocalityDistrict(parentLocalityDistrictId);
        return localityPartService.getLocalityPartsForParentLocalityDistrict(localityDistrict)
                .stream()
                .map(this::mapToLocalityPart)
                .collect(Collectors.toList());
    }

    private LocalityPartDTO mapToLocalityPart(LocalityPart localityPart) {
        return new LocalityPartDTO(localityPart.getId(), localityPart.getName(), localityPart.getLocalityType());
    }

}
