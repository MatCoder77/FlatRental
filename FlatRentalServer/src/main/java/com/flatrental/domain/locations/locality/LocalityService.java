package com.flatrental.domain.locations.locality;

import com.google.common.collect.Lists;
import com.flatrental.domain.locations.abstractlocality.AbstractLocality;
import com.flatrental.domain.locations.abstractlocality.GenericLocalityType;
import com.flatrental.domain.locations.commune.Commune;
import com.flatrental.domain.locations.commune.CommuneService;
import com.flatrental.domain.locations.district.District;
import com.flatrental.domain.locations.localitydistrict.LocalityDistrictService;
import com.flatrental.domain.locations.localitypart.LocalityPartService;
import com.flatrental.domain.locations.localitytype.LocalityType;
import com.flatrental.domain.locations.localitytype.LocalityTypeService;
import com.flatrental.domain.locations.teryt.simc.LocalityDTO;
import com.flatrental.domain.locations.teryt.simc.LocalityDTOService;
import com.flatrental.domain.locations.teryt.ulic.Update;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocalityService {

    private final LocalityDTOService localityDTOService;
    private final LocalityRepository localityRepository;
    private final CommuneService communeService;
    private final LocalityTypeService localityTypeService;
    private final LocalityDistrictService localityDistrictService;
    private final LocalityPartService localityPartService;

    private static final int MAX_SAVED_AT_ONCE_SIZE = 1000;
    private static final String SUPPLIED_LOCALITY_IS_NOT_AUTONOMOUS = "Supplied locality {0} is not autonomous";
    private static final String THERE_IS_NO_LOCALITY_WITH_SUPPLIED_CODE = "There is no locality with code {0}";
    private static final String THERE_IS_NO_LOCALITY_WITH_ID = "There is no locality with id {0}";

    public List<Locality> createLocalities(List<LocalityDTO> localityDTOs,
                                           Map<String, Map<String, District>> districtByCodeGroupedByVoivodeshipCode,
                                           Map<District, Map<String, Commune>> communeByCodeGroupedByDistrict,
                                           Map<String, LocalityType> localityTypeByCode) {
        List<Locality> localities = localityDTOs.stream()
                .map(localityDTO -> mapToLocality(localityDTO, districtByCodeGroupedByVoivodeshipCode, communeByCodeGroupedByDistrict, localityTypeByCode))
                .collect(Collectors.toList());

        return Lists.partition(localities, MAX_SAVED_AT_ONCE_SIZE).stream()
                .map(localityRepository::saveAll)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private Locality mapToLocality(LocalityDTO localityDTO,
                                   Map<String, Map<String, District>> districtByCodeGroupedByVoivodeshipCode,
                                   Map<District, Map<String, Commune>> communeByCodeGroupedByDistrict,
                                   Map<String, LocalityType> localityTypeByCode) {
        String voivodeshipCode = localityDTO.getVoivodeshipCode();
        String districtCode = localityDTO.getDistrictCode();
        District district  = districtByCodeGroupedByVoivodeshipCode.get(voivodeshipCode).get(districtCode);
        String communeCode = localityDTO.getCommuneCode();
        Commune commune = communeByCodeGroupedByDistrict.get(district).get(communeCode);
        LocalityType localityType = localityTypeByCode.get(localityDTO.getLocalityTypeCode());
        return mapToLocality(localityDTO, commune, localityType);
    }

    private Locality mapToLocality(LocalityDTO localityDTO, Commune commune, LocalityType localityType) {
        validateLocality(localityDTO);
        return new Locality(localityType, localityDTO.getHasCustomaryName(), localityDTO.getName(), localityDTO.getLocalityCode(), commune);
    }

    private void validateLocality(LocalityDTO localityDTO) {
        if (!localityDTOService.isAutonomousLocality(localityDTO)) {
            throw new IllegalArgumentException(MessageFormat.format(SUPPLIED_LOCALITY_IS_NOT_AUTONOMOUS, localityDTO));
        }
    }

    public Locality createLocality(LocalityDTO localityDTO) {
        Commune commune = communeService.getExistingCommune(localityDTO.getCommuneCode(), localityDTO.getDistrictCode(), localityDTO.getVoivodeshipCode());
        LocalityType localityType = localityTypeService.getExistingLocalityType(localityDTO.getLocalityTypeCode());
        Locality locality = mapToLocality(localityDTO, commune, localityType);
        return localityRepository.save(locality);
    }

    public void updateLocality(Update<AbstractLocality, LocalityDTO> update) {
        AbstractLocality abstractLocality = update.getEntityBeforeUpdate();
        LocalityDTO localityAfterChange = update.getStateAfterUpdate();
        if (willBeLocalityDistrict(localityAfterChange)) {
            abstractLocality.setGenericLocalityType(GenericLocalityType.LOCALITY_DISTRICT);
            localityDistrictService.updateLocalityDistrict(new Update<>(abstractLocality, localityAfterChange));
        } else if (willBeLocalityPart(localityAfterChange)) {
            abstractLocality.setGenericLocalityType(GenericLocalityType.LOCALITY_PART);
            localityPartService.updateLocalityPart(new Update<>(abstractLocality, localityAfterChange));
        } else {
            Commune commune = communeService.getExistingCommune(localityAfterChange.getCommuneCode(), localityAfterChange.getDistrictCode(), localityAfterChange.getVoivodeshipCode());
            LocalityType localityType = localityTypeService.getExistingLocalityType(localityAfterChange.getLocalityTypeCode());
            abstractLocality.setCommune(commune);
            abstractLocality.setLocalityType(localityType);
            abstractLocality.setHasCustomaryName(localityAfterChange.getHasCustomaryName());
            abstractLocality.setName(localityAfterChange.getName());
            abstractLocality.setCode(localityAfterChange.getLocalityCode());
            Locality locality = Locality.fromAbstractLocality(abstractLocality);
            localityRepository.save(locality);
        }
    }

    public Locality getExistingLocality(String code) {
        return localityRepository.findLocalityByCode(code)
                .orElseThrow(() -> new IllegalArgumentException(MessageFormat.format(THERE_IS_NO_LOCALITY_WITH_SUPPLIED_CODE, code)));
    }

    private boolean willBeLocalityPart(LocalityDTO localityDTO) {
        return localityDTOService.isLocalityPart(localityDTO);
    }

    private boolean willBeLocalityDistrict(LocalityDTO localityDTO) {
        return localityDTOService.isLocalityDistrict(localityDTO);
    }

    public Optional<Locality> findLocality(String code) {
        return localityRepository.findLocalityByCode(code);
    }

    public Locality getParentLocalityForLocalityDistrict(LocalityDTO localityDistrictDTO) {
        List<Locality> localities = localityRepository.findLocalityByCommuneDistrictCode(localityDistrictDTO.getDistrictCode());
        if (localities.size() == 1) {
            return localities.get(0);
        }
        throw new IllegalArgumentException("Found more than one city with district rights");
    }

    public void deleteLocality(Locality locality) {
        localityRepository.deleteLocality(locality);
    }

    public List<Locality> getAllLocalities() {
        return localityRepository.getAllLocalities();
    }

    public List<Locality> getLocalitiesForCommune(Commune commune) {
        return localityRepository.getLocalitiesForCommune(commune);
    }

    public Locality getExistingLocality(Long id) {
        return localityRepository.findLocalityById(id)
                .orElseThrow(() -> new IllegalArgumentException(MessageFormat.format(THERE_IS_NO_LOCALITY_WITH_ID, id)));
    }

    public com.flatrental.api.LocalityDTO mapToLocalityDTO(Locality locality) {
        return new com.flatrental.api.LocalityDTO(locality.getId(), locality.getName(), locality.getLocalityType());
    }

}
