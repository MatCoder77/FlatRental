package com.flatrental.domain.locations.district;


import com.flatrental.api.DistrictDTO;
import com.flatrental.domain.locations.voivodeship.Voivodeship;
import com.flatrental.domain.locations.voivodeship.VoivodeshipService;
import com.flatrental.domain.locations.teryt.terc.AdministrationUnitDTO;
import com.flatrental.domain.locations.teryt.terc.AdministrationUnitService;
import com.flatrental.domain.locations.teryt.ulic.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DistrictService {

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private AdministrationUnitService administrationUnitService;

    @Autowired
    private VoivodeshipService voivodeshipService;

    private static final String SUPPLIED_ADMINISTRATION_UNIT_IS_NOT_DISTRICT = "Supplied unit {0} is not of district type.";
    private static final String THERE_IS_NO_DISTRICT_WITH_SUPPLIED_CODES = "District with voivodeship code {0} and district code {1} does not exist";
    private static final String THERE_IS_NO_DISTRICT_WITH_ID = "There is no district with id {0}";


    public List<District> createDistricts(List<AdministrationUnitDTO> districtDTOs, Map<String, Voivodeship> voivodeshipByCode) {
        List<District> districts = districtDTOs.stream()
                .map(district -> mapToDistrict(district, voivodeshipByCode.get(district.getVoivodeshipCode())))
                .collect(Collectors.toList());
        return districtRepository.saveAll(districts);
    }

    private District mapToDistrict(AdministrationUnitDTO districtDTO, Voivodeship voivodeship) {
        validateDistrictDTO(districtDTO);
        return new District(districtDTO.getDistrictCode(), districtDTO.getName(), voivodeship, DistrictType.fromAdministrationUnitTypeDTO(districtDTO.getUnitType()));
    }

    private void validateDistrictDTO(AdministrationUnitDTO districtDTO) {
        if (!administrationUnitService.isDistrictUnit(districtDTO)) {
            throw new IllegalArgumentException(MessageFormat.format(SUPPLIED_ADMINISTRATION_UNIT_IS_NOT_DISTRICT, districtDTO));
        }
    }

    public District createDistrict(AdministrationUnitDTO districtDTO) {
        Voivodeship parentVoivodeship = voivodeshipService.getExistingVoivodeship(districtDTO.getVoivodeshipCode());
        District district = mapToDistrict(districtDTO, parentVoivodeship);
        return districtRepository.save(district);
    }

    public District getExistingDistrict(AdministrationUnitDTO districtDTO) {
        return getExistingDistrict(districtDTO.getDistrictCode(), districtDTO.getVoivodeshipCode());
    }

    public District getExistingDistrict(String districtCode, String voivodeshipCode) {
        return districtRepository.getDistrictByCodeAndVoivodeshipCode(districtCode, voivodeshipCode)
                .orElseThrow(() -> new IllegalArgumentException(MessageFormat.format(THERE_IS_NO_DISTRICT_WITH_SUPPLIED_CODES, voivodeshipCode, districtCode)));
    }

    public void updateDistrict(Update<District, AdministrationUnitDTO> update) {
        District district = update.getEntityBeforeUpdate();
        AdministrationUnitDTO districtAfterChange = update.getStateAfterUpdate();
        Voivodeship voivodeship = voivodeshipService.getExistingVoivodeship(districtAfterChange.getVoivodeshipCode());
        district.setVoivodeship(voivodeship);
        district.setCode(districtAfterChange.getDistrictCode());
        district.setName(districtAfterChange.getName());
        district.setType(DistrictType.fromAdministrationUnitTypeDTO(districtAfterChange.getUnitType()));
        districtRepository.save(district);
    }

    public void deleteDistrict(District districtToDelete) {
        districtRepository.delete(districtToDelete);
    }

    public List<District> getAllDistricts() {
        return districtRepository.findAll();
    }

    public List<District> getDistrictsForVoivodeship(Voivodeship voivodeship) {
        return districtRepository.getAllByVoivodeship(voivodeship);
    }

    public District getExistingDistrict(Long id) {
        return districtRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(MessageFormat.format(THERE_IS_NO_DISTRICT_WITH_ID, id)));
    }

    public DistrictDTO mapToDistrictDTO(District district) {
        return new DistrictDTO(district.getId(), district.getName(), district.getType());
    }

}
