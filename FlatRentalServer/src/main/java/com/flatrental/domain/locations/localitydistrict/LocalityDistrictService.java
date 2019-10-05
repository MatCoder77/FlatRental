package com.flatrental.domain.locations.localitydistrict;

import com.flatrental.api.LocalityDistrictDTO;
import com.flatrental.domain.locations.abstractlocality.AbstractLocality;
import com.flatrental.domain.locations.abstractlocality.GenericLocalityType;
import com.flatrental.domain.locations.district.District;
import com.flatrental.domain.locations.district.DistrictType;
import com.flatrental.domain.locations.locality.Locality;
import com.flatrental.domain.locations.locality.LocalityService;
import com.flatrental.domain.locations.localitypart.LocalityPartService;
import com.flatrental.domain.locations.localitytype.LocalityType;
import com.flatrental.domain.locations.localitytype.LocalityTypeService;
import com.flatrental.domain.locations.teryt.simc.LocalityDTO;
import com.flatrental.domain.locations.teryt.simc.LocalityDTOService;
import com.flatrental.domain.locations.teryt.ulic.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class LocalityDistrictService {

    @Autowired
    private LocalityDistrictRepository localityDistrictRepository;

    @Autowired
    private LocalityDTOService localityDTOService;

    @Autowired
    private LocalityService localityService;

    @Autowired
    private LocalityTypeService localityTypeService;

    @Autowired
    private LocalityPartService localityPartService;


    private static final String SUPPLIED_LOCALITY_IS_NOT_LOCALITY_DISTRICT = "Supplied locality {0} is locality district";
    private static final String THERE_IS_NO_LOCALITY_DISTRICT_WITH_SUPPLIED_CODE = "There is no locality district with code {0}";


    public List<LocalityDistrict> createLocalityDistricts(List<LocalityDTO> localityDistrictDTOs,
                                                          List<Locality> autonomousLocalities,
                                                          Map<String, Map<String, District>> districtByCodeGroupedByVoivodeshipCode,
                                                          Map<String, LocalityType> localityTypeByCode) {
        Map<District, Locality> autonomousCitiesWithDistrictRightsByDistrict = autonomousLocalities.stream()
                .filter(this::isAutonomousCityWithDistrictRights)
                .collect(Collectors.toMap(locality -> locality.getCommune().getDistrict(), Function.identity()));

        List<LocalityDistrict> localityDistricts = localityDistrictDTOs.stream()
                .map(localityDistrictDTO -> mapToLocalityDistrict(
                        localityDistrictDTO,
                        getParentLocality(localityDistrictDTO, autonomousCitiesWithDistrictRightsByDistrict, districtByCodeGroupedByVoivodeshipCode),
                        localityTypeByCode.get(localityDistrictDTO.getLocalityTypeCode())))
                .collect(Collectors.toList());

        return localityDistrictRepository.saveAll(localityDistricts);
    }

    private Locality getParentLocality(LocalityDTO localityDistrictDTO,
                                       Map<District, Locality> autonomousCitiesWithDistrictRightsByDistrict,
                                       Map<String, Map<String,District>> districtByCodeGroupedByVoivodeshipCode) {
        String voivodeshipCode = localityDistrictDTO.getVoivodeshipCode();
        String districtCode = localityDistrictDTO.getDistrictCode();
        District district = districtByCodeGroupedByVoivodeshipCode.get(voivodeshipCode).get(districtCode);
        return autonomousCitiesWithDistrictRightsByDistrict.get(district);
    }

    private boolean isAutonomousCityWithDistrictRights(Locality locality) {
        return locality.getCommune().getDistrict().getType().equals(DistrictType.DISTRICT_CITY) ||
                locality.getCommune().getDistrict().getType().equals(DistrictType.DISTRICT_CAPITAL);
    }

    private LocalityDistrict mapToLocalityDistrict(LocalityDTO localityDTO, Locality parentLocality, LocalityType localityType) {
        validateLocalityDistrict(localityDTO);
        return new LocalityDistrict(localityType, localityDTO.getHasCustomaryName(), localityDTO.getName(), localityDTO.getLocalityCode(), parentLocality);
    }

    private void validateLocalityDistrict(LocalityDTO localityDTO) {
        if (!localityDTOService.isLocalityDistrict(localityDTO)) {
            throw new IllegalArgumentException(MessageFormat.format(SUPPLIED_LOCALITY_IS_NOT_LOCALITY_DISTRICT, localityDTO));
        }
    }

    public LocalityDistrict createLocalityDistrict(LocalityDTO districtDTO) {
        Locality parentLocality = getParentLocality(districtDTO);
        LocalityType localityType = localityTypeService.getExistingLocalityType(districtDTO.getLocalityTypeCode());
        LocalityDistrict localityDistrict = mapToLocalityDistrict(districtDTO, parentLocality, localityType);
        return localityDistrictRepository.save(localityDistrict);
    }

    private Locality getParentLocality(LocalityDTO localityDistrictDTO) {
        return localityService.getParentLocalityForLocalityDistrict(localityDistrictDTO);
    }

    public LocalityDistrict getExistingLocalityDistrict(String code) {
        return localityDistrictRepository.findLocalityDistrictByCode(code)
                .orElseThrow(() -> new IllegalArgumentException(MessageFormat.format(THERE_IS_NO_LOCALITY_DISTRICT_WITH_SUPPLIED_CODE, code)));
    }

    public Optional<LocalityDistrict> findLocalityDistrict(String code) {
        return localityDistrictRepository.findLocalityDistrictByCode(code);
    }

    public void updateLocalityDistrict(Update<AbstractLocality, LocalityDTO> update) {
        AbstractLocality abstractLocality = update.getEntityBeforeUpdate();
        LocalityDTO localityDistrictAfterChange = update.getStateAfterUpdate();
        if (willBeChangedToLocality(localityDistrictAfterChange)) {
            abstractLocality.setGenericLocalityType(GenericLocalityType.AUTONOMOUS_LOCALITY);
            localityService.updateLocality(new Update<>(abstractLocality, localityDistrictAfterChange));
        } else if (willBeChangedToLocalityPart(localityDistrictAfterChange)) {
            abstractLocality.setGenericLocalityType(GenericLocalityType.LOCALITY_PART);
            localityPartService.updateLocalityPart(new Update<>(abstractLocality, localityDistrictAfterChange));
        } else {
            Locality locality = getParentLocality(localityDistrictAfterChange);
            LocalityType localityType = localityTypeService.getExistingLocalityType(localityDistrictAfterChange.getLocalityTypeCode());
            abstractLocality.setParentLocality(AbstractLocality.fromLocality(locality));
            abstractLocality.setLocalityType(localityType);
            abstractLocality.setName(localityDistrictAfterChange.getName());
            abstractLocality.setHasCustomaryName(localityDistrictAfterChange.getHasCustomaryName());
            abstractLocality.setCode(localityDistrictAfterChange.getLocalityCode());
            LocalityDistrict localityDistrict = LocalityDistrict.fromAbstractLocality(abstractLocality);
            localityDistrictRepository.save(localityDistrict);
        }
    }

    private boolean willBeChangedToLocality(LocalityDTO localityDTO) {
        return localityDTOService.isAutonomousLocality(localityDTO);
    }

    private boolean willBeChangedToLocalityPart(LocalityDTO localityDTO) {
        return localityDTOService.isLocalityPart(localityDTO);
    }

    public void deleteLocalityDistrict(LocalityDistrict localityDistrict) {
        localityDistrictRepository.deleteLocalityDistrict(localityDistrict);
    }

    public List<LocalityDistrict> getAllLocalityDistricts() {
        return localityDistrictRepository.getAllLocalityDistricts();
    }

    public List<LocalityDistrict> getAllLocalityDistrictsForLocality(Locality locality) {
        return localityDistrictRepository.getAllLocalityDistrictsForLocality(locality);
    }

    public LocalityDistrict getExistingLocalityDistrict(Long id) {
        return localityDistrictRepository.getExistingLocalityDistrict(id);
    }

    public LocalityDistrictDTO mapToLocalityDistrictDTO(LocalityDistrict localityDistrict) {
        return new LocalityDistrictDTO(localityDistrict.getId(), localityDistrict.getName());
    }

}
