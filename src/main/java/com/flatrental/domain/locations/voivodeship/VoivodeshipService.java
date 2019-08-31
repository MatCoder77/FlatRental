package com.flatrental.domain.locations.voivodeship;

import com.flatrental.teryt_api.payloads.terc.AdministrationUnitDTO;
import com.flatrental.teryt_api.payloads.terc.AdministrationUnitService;
import com.flatrental.teryt_api.payloads.ulic.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VoivodeshipService {

    @Autowired
    private VoivodeshipRepository voivodeshipRepository;

    @Autowired
    private AdministrationUnitService administrationUnitService;


    private static final String SUPPLIED_ADMINISTRATION_UNIT_IS_NOT_VOIVODESHIP = "Supplied administration unit {0} is not of voivodeship type!";
    private static final String VOIVODESHIP_WITH_SUPPLIED_ID_DOES_NOT_EXIST = "Voivodeship with supplied code {0} does not exist!";

    public List<Voivodeship> createVoivodeships(List<AdministrationUnitDTO> voivodeshipDTOs) {
        List<Voivodeship> voivodeships = voivodeshipDTOs.stream()
                .map(this::mapToVoivodeship)
                .collect(Collectors.toList());
        return voivodeshipRepository.saveAll(voivodeships);
    }

    private Voivodeship mapToVoivodeship(AdministrationUnitDTO voivodeshipDTO) {
        validateVoivodeshipDTO(voivodeshipDTO);
        return new Voivodeship(voivodeshipDTO.getVoivodeshipCode(), voivodeshipDTO.getName());
    }

    private void validateVoivodeshipDTO(AdministrationUnitDTO voivodeshipDTO) {
        if (!administrationUnitService.isVoivodeshipUnit(voivodeshipDTO)) {
            throw new IllegalArgumentException(MessageFormat.format(SUPPLIED_ADMINISTRATION_UNIT_IS_NOT_VOIVODESHIP, voivodeshipDTO));
        }
    }

    public Voivodeship createVoivodeship(AdministrationUnitDTO voivodeshipDTO) {
        Voivodeship voivodeship = mapToVoivodeship(voivodeshipDTO);
        return voivodeshipRepository.save(voivodeship);
    }

    public Voivodeship getExistingVoivodeship(String code) {
        return voivodeshipRepository.getVoivodeshipByCode(code)
                .orElseThrow(() -> new IllegalArgumentException(MessageFormat.format(VOIVODESHIP_WITH_SUPPLIED_ID_DOES_NOT_EXIST, code)));
    }

    public void updateVoivodeship(Update<Voivodeship, AdministrationUnitDTO> update) {
        Voivodeship voivodeship = update.getEntityBeforeUpdate();
        AdministrationUnitDTO voivodeshipAfterChange = update.getStateAfterUpdate();
        voivodeship.setCode(voivodeshipAfterChange.getVoivodeshipCode());
        voivodeship.setName(voivodeshipAfterChange.getName());
        voivodeshipRepository.save(voivodeship);
    }

    public void deleteVoivodeship(Voivodeship voivodeshipToDelete) {
        voivodeshipRepository.delete(voivodeshipToDelete);
    }

    public List<Voivodeship> getAllVoivodeships() {
        return voivodeshipRepository.findAll();
    }

}

