package com.flatrental.domain.locations.localitypart;

import com.flatrental.api.LocalityPartDTO;
import com.flatrental.domain.locations.abstractlocality.AbstractLocality;
import com.flatrental.domain.locations.abstractlocality.GenericLocalityType;
import com.flatrental.domain.locations.locality.Locality;
import com.flatrental.domain.locations.locality.LocalityService;
import com.flatrental.domain.locations.localitydistrict.LocalityDistrict;
import com.flatrental.domain.locations.localitydistrict.LocalityDistrictService;
import com.flatrental.domain.locations.localitytype.LocalityType;
import com.flatrental.domain.locations.localitytype.LocalityTypeService;
import com.flatrental.domain.locations.teryt.simc.LocalityDTO;
import com.flatrental.domain.locations.teryt.simc.LocalityDTOService;
import com.flatrental.domain.locations.teryt.ulic.Update;
import com.google.common.collect.Lists;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LocalityPartService {

    private final LocalityPartRepository localityPartRepository;
    private final LocalityDTOService localityDTOService;
    private final LocalityService localityService;
    private final LocalityDistrictService localityDistrictService;
    private final LocalityTypeService localityTypeService;

    private static final int MAX_SAVED_AT_ONCE_SIZE = 1000;
    private static final String SUPPLIED_LOCALITY_IS_NOT_LOCALITY_PART = "Supplied locality {0} is locality part";
    private static final String THERE_IN_NO_LOCALITY_PART_WITH_SUPPLIED_CODE = "There is no locality part with code {0}";
    private static final String THERE_IN_NO_LOCALITY_PART_WITH_SUPPLIED_ID = "There is no locality part with id {0}";

    public LocalityPartService(LocalityPartRepository localityPartRepository,
                               LocalityDTOService localityDTOService,
                               @Lazy LocalityService localityService,
                               @Lazy LocalityDistrictService localityDistrictService,
                               LocalityTypeService localityTypeService) {
        this.localityPartRepository = localityPartRepository;
        this.localityDTOService = localityDTOService;
        this.localityService = localityService;
        this.localityDistrictService = localityDistrictService;
        this.localityTypeService = localityTypeService;
    }

    public List<LocalityPart> createLocalityParts(List<LocalityDTO> localityPartDTOs,
                                                  Map<String, Locality> localityByCode,
                                                  Map<String, LocalityDistrict> localityDistrictByCode,
                                                  Map<String, LocalityType> localityTypeByCode) {
        List<LocalityPart> localityParts = localityPartDTOs.stream()
                .map(localityPartDTO -> mapToLocalityPart(
                        localityPartDTO,
                        getLocalityFromDirectParentCode(localityPartDTO.getDirectParentCode(), localityByCode, localityDistrictByCode),
                        getLocalityDistrictIfItIsDirectParentOrNullOtherwise(localityPartDTO.getDirectParentCode(), localityDistrictByCode),
                        localityTypeByCode.get(localityPartDTO.getLocalityTypeCode())))
                .collect(Collectors.toList());

        return Lists.partition(localityParts, MAX_SAVED_AT_ONCE_SIZE).stream()
                .map(localityPartRepository::saveAll)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private Locality getLocalityFromDirectParentCode(String directParentCode, Map<String, Locality> localityByCode, Map<String, LocalityDistrict> localityDistrictByCode) {
        if (isDirectParentAutonomousLocality(directParentCode, localityByCode)) {
            return localityByCode.get(directParentCode);
        }
        return getLocalityFromLocalityDistrict(directParentCode, localityDistrictByCode);
    }

    private boolean isDirectParentAutonomousLocality(String directParentCode, Map<String, Locality> localityByCode) {
        return localityByCode.containsKey(directParentCode);
    }

    private Locality getLocalityFromLocalityDistrict(String districtCode, Map<String, LocalityDistrict> localityDistrictByCode) {
        return localityDistrictByCode.get(districtCode).getParentLocality();
    }

    private LocalityDistrict getLocalityDistrictIfItIsDirectParentOrNullOtherwise(String directParentCode, Map<String, LocalityDistrict> localityDistrictByCode) {
        return localityDistrictByCode.get(directParentCode);
    }

    private LocalityPart mapToLocalityPart(LocalityDTO localityDTO, Locality locality, LocalityDistrict localityDistrict, LocalityType localityType) {
        validateLocalityPart(localityDTO);
        return new LocalityPart(localityType, localityDTO.getHasCustomaryName(), localityDTO.getName(), localityDTO.getLocalityCode(), locality, localityDistrict);
    }

    private void validateLocalityPart(LocalityDTO localityDTO) {
        if (!localityDTOService.isLocalityPart(localityDTO)) {
            throw new IllegalArgumentException(MessageFormat.format(SUPPLIED_LOCALITY_IS_NOT_LOCALITY_PART, localityDTO));
        }
    }

    public LocalityPart createLocalityPart(LocalityDTO localityPartDTO) {
        Locality locality = getLocalityFromDirectParentCode(localityPartDTO.getDirectParentCode());
        LocalityDistrict localityDistrict = getLocalityDistrictIfItIsDirectParentOrNullOtherwise(localityPartDTO.getDirectParentCode());
        LocalityType localityType = localityTypeService.getExistingLocalityType(localityPartDTO.getLocalityTypeCode());
        LocalityPart localityPart = mapToLocalityPart(localityPartDTO, locality, localityDistrict, localityType);
        return localityPartRepository.save(localityPart);
    }

    private Locality getLocalityFromDirectParentCode(String directParentCode) {
        return localityService.findLocality(directParentCode)
                .orElseGet(() -> getLocalityFromLocalityDistrict(directParentCode));
    }

    private Locality getLocalityFromLocalityDistrict(String localityDistrictCode) {
        return localityDistrictService.getExistingLocalityDistrict(localityDistrictCode).getParentLocality();
    }

    private LocalityDistrict getLocalityDistrictIfItIsDirectParentOrNullOtherwise(String directParentCode) {
        return localityDistrictService.findLocalityDistrict(directParentCode).orElse(null);
    }

    public void updateLocalityPart(Update<AbstractLocality, LocalityDTO> update) {
        AbstractLocality abstractLocality = update.getEntityBeforeUpdate();
        LocalityDTO localityPartAfterChange = update.getStateAfterUpdate();
        if (willBeChangedToAutonomousLocality(localityPartAfterChange)) {
            abstractLocality.setGenericLocalityType(GenericLocalityType.AUTONOMOUS_LOCALITY);
            localityService.updateLocality(new Update<>(abstractLocality, localityPartAfterChange));
        } else if (willBeChangedToLocalityDistrict(localityPartAfterChange)) {
            abstractLocality.setGenericLocalityType(GenericLocalityType.LOCALITY_DISTRICT);
            localityDistrictService.updateLocalityDistrict(new Update<>(abstractLocality, localityPartAfterChange));
        } else {
            Locality locality = getLocalityFromDirectParentCode(localityPartAfterChange.getDirectParentCode());
            LocalityDistrict localityDistrict = getLocalityDistrictIfItIsDirectParentOrNullOtherwise(localityPartAfterChange.getDirectParentCode());
            LocalityType localityType = localityTypeService.getExistingLocalityType(localityPartAfterChange.getLocalityTypeCode());
            abstractLocality.setParentLocality(AbstractLocality.fromLocality(locality));
            abstractLocality.setLocalityDistrict(Optional.ofNullable(localityDistrict).map(AbstractLocality::fromLocalityDistrict).orElse(null));
            abstractLocality.setLocalityType(localityType);
            abstractLocality.setName(localityPartAfterChange.getName());
            abstractLocality.setHasCustomaryName(localityPartAfterChange.getHasCustomaryName());
            abstractLocality.setCode(localityPartAfterChange.getLocalityCode());
            LocalityPart localityPart = LocalityPart.fromAbstractLocality(abstractLocality);
            localityPartRepository.save(localityPart);
        }
    }

    public LocalityPart getExistingLocalityPart(String code) {
        return localityPartRepository.findLocalityPartByCode(code)
                .orElseThrow(() -> new IllegalArgumentException(MessageFormat.format(THERE_IN_NO_LOCALITY_PART_WITH_SUPPLIED_CODE, code)));
    }

    public LocalityPart getExistingLocalityPart(Long id) {
        return localityPartRepository.findLocalityPartById(id)
                .orElseThrow(() -> new IllegalArgumentException(MessageFormat.format(THERE_IN_NO_LOCALITY_PART_WITH_SUPPLIED_ID, id)));
    }

    private boolean willBeChangedToAutonomousLocality(LocalityDTO localityDTO) {
        return localityDTOService.isAutonomousLocality(localityDTO);
    }

    private boolean willBeChangedToLocalityDistrict(LocalityDTO localityDTO) {
        return  localityDTOService.isLocalityDistrict(localityDTO);
    }

    public void deleteLocalityPart(LocalityPart localityPart) {
        localityPartRepository.deleteLocalityPart(localityPart);
    }

    public List<LocalityPart> getAllLocalityParts() {
        return localityPartRepository.getAllLocalityParts();
    }

    public List<LocalityPart> getLocalityPartsForParentLocality(Locality parentLocality) {
        return localityPartRepository.getLocalityPartsForParentLocality(parentLocality);
    }

    public List<LocalityPart> getLocalityPartsForParentLocalityDistrict(LocalityDistrict parentLocalityDistrict) {
        return localityPartRepository.getLocalityPartsForParentLocalityDistrict(parentLocalityDistrict);
    }

    public LocalityPartDTO mapToLocalityPart(LocalityPart localityPart) {
        return new LocalityPartDTO(localityPart.getId(), localityPart.getName(), localityPart.getLocalityType());
    }

}
