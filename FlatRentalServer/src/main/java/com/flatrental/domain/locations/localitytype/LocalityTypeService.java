package com.flatrental.domain.locations.localitytype;

import com.flatrental.domain.locations.teryt.simc.LocalityTypeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocalityTypeService {

    private final LocalityTypeRepository localityTypeRepository;

    private static final String THERE_IS_NO_LOCALITY_TYPE_WITH_SUPPLIED_CODE = "Locality type with code {1} does not exist";

    public List<LocalityType> createLocalityTypes(List<LocalityTypeDTO> localityTypeDTOs) {
        List<LocalityType> localityTypes = localityTypeDTOs.stream()
                .map(this::mapToLocalityType)
                .collect(Collectors.toList());
        return localityTypeRepository.saveAll(localityTypes);
    }

    private LocalityType mapToLocalityType(LocalityTypeDTO localityTypeDTO) {
        return new LocalityType(localityTypeDTO.getCode(), localityTypeDTO.getName());
    }

    private LocalityType createLocalityType(LocalityTypeDTO localityTypeDTO) {
        LocalityType localityType = mapToLocalityType(localityTypeDTO);
        return localityTypeRepository.save(localityType);
    }

    public LocalityType getExistingLocalityType(String typeCode) {
        return localityTypeRepository.getLocalityTypeByTypeCode(typeCode)
                .orElseThrow(() -> new IllegalArgumentException(MessageFormat.format(THERE_IS_NO_LOCALITY_TYPE_WITH_SUPPLIED_CODE, typeCode)));
    }

}
