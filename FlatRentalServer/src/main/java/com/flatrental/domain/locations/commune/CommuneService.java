package com.flatrental.domain.locations.commune;

import com.flatrental.api.CommuneDTO;
import com.flatrental.domain.locations.district.District;
import com.flatrental.domain.locations.district.DistrictService;
import com.flatrental.domain.locations.teryt.terc.AdministrationUnitDTO;
import com.flatrental.domain.locations.teryt.terc.AdministrationUnitService;
import com.flatrental.domain.locations.teryt.ulic.Update;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommuneService {

    private final CommuneRepository communeRepository;
    private final AdministrationUnitService administrationUnitService;
    private final DistrictService districtService;


    private static final String SUPPLIED_ADMINISTRATION_UNIT_IS_NOT_COMMUNE = "Supplied unit {0} is not of commune type.";
    private static final String THERE_IS_NO_COMMUNE_WITH_SUPPLIED_CODES = "Commune with voivodeship code {0} and district code {1} and commune code {2} does not exist";
    private static final String THERE_IS_NO_COMMUNE_WITH_ID = "There is no commune with id {0}";


    public List<Commune> createCommunes(List<AdministrationUnitDTO> communesDTOs, Map<String, Map<String, District>> districtByCodeGroupedByVoivodeshipCode) {
        List<Commune> communes = communesDTOs.stream()
                .map(communeDTO -> mapToCommune(communeDTO, getDistrictForCommune(communeDTO, districtByCodeGroupedByVoivodeshipCode)))
                .collect(Collectors.toList());
        return communeRepository.saveAll(communes);
    }

    private District getDistrictForCommune(AdministrationUnitDTO communeDTO, Map<String, Map<String,District>> districtByCodeGroupedByVoivodeshipCode) {
        return districtByCodeGroupedByVoivodeshipCode.get(communeDTO.getVoivodeshipCode()).get(communeDTO.getDistrictCode());
    }

    private Commune mapToCommune(AdministrationUnitDTO communeDTO, District district) {
        validateCommuneDTO(communeDTO);
        return new Commune(communeDTO.getCommuneCode(), communeDTO.getName(), CommuneType.fromAdministrationUnitTypeDTO(communeDTO.getUnitType()), district);
    }

    private void validateCommuneDTO(AdministrationUnitDTO communeDTO) {
        if (!administrationUnitService.isCommuneUnit(communeDTO)) {
            throw new IllegalArgumentException(MessageFormat.format(SUPPLIED_ADMINISTRATION_UNIT_IS_NOT_COMMUNE, communeDTO));
        }
    }

    public Commune createCommune(AdministrationUnitDTO communeDTO) {
        District district = districtService.getExistingDistrict(communeDTO.getDistrictCode(), communeDTO.getVoivodeshipCode());
        Commune commune = mapToCommune(communeDTO, district);
        return communeRepository.save(commune);
    }

    public void updateCommune(Update<Commune, AdministrationUnitDTO> update) {
        Commune commune = update.getEntityBeforeUpdate();
        AdministrationUnitDTO communeAfterChange = update.getStateAfterUpdate();
        District district = districtService.getExistingDistrict(communeAfterChange.getDistrictCode(), communeAfterChange.getVoivodeshipCode());
        commune.setDistrict(district);
        commune.setCode(communeAfterChange.getCommuneCode());
        commune.setName(communeAfterChange.getName());
        commune.setType(CommuneType.fromAdministrationUnitTypeDTO(communeAfterChange.getUnitType()));
        communeRepository.save(commune);
    }

    public Commune getExistingCommune(AdministrationUnitDTO communeDTO) {
        return getExistingCommune(communeDTO.getCommuneCode(), communeDTO.getDistrictCode(), communeDTO.getVoivodeshipCode());
    }

    public Commune getExistingCommune(String communeCode, String districtCode, String voivodeshipCode) {
        District district = districtService.getExistingDistrict(districtCode, voivodeshipCode);
        return communeRepository.getCommuneByCodeAndDistrictId(communeCode, district.getId())
                .orElseThrow(() -> new IllegalArgumentException(MessageFormat.format(THERE_IS_NO_COMMUNE_WITH_SUPPLIED_CODES, voivodeshipCode, districtCode, communeCode)));
    }

    public void deleteCommune(Commune communeToDelete) {
        communeRepository.delete(communeToDelete);
    }

    public List<Commune> getAllCommunes() {
        return communeRepository.findAll();
    }

    public Commune getExistingCommune(Long id) {
        return communeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(MessageFormat.format(THERE_IS_NO_COMMUNE_WITH_ID, id)));
    }

    public List<Commune> getCommunesForDistrict(District district) {
        return communeRepository.getCommunesByDistrict(district);
    }

    public CommuneDTO mapToCommuneDTO(Commune commune) {
        return new CommuneDTO(commune.getId(), commune.getName(), commune.getType());
    }

}
