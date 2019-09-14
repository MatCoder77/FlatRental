package com.flatrental.domain.locations.localitydistrict;


import com.flatrental.domain.locations.abstractlocality.AbstractLocality;
import com.flatrental.domain.locations.abstractlocality.AbstractLocalityRepository;
import com.flatrental.domain.locations.abstractlocality.GenericLocalityType;
import com.flatrental.domain.locations.locality.Locality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class LocalityDistrictRepository {

    @Autowired
    private AbstractLocalityRepository abstractLocalityRepository;

    private static final String LOCALITY_DISTRICT_WITH_CODE_DOESNT_EXIST = "LocalityDistrict with code {0} doesn't exist";
    private static final String LOCALITY_DISTRICT_WITH_ID_DOESNT_EXIST = "LocalityDistrict with id {0} doesn't exist";

    Optional<LocalityDistrict> findLocalityDistrictByCode(String code) {
        return abstractLocalityRepository.findAbstractLocalityByCodeAndGenericLocalityType(code, GenericLocalityType.LOCALITY_DISTRICT)
                .map(LocalityDistrict::fromAbstractLocality);
    }

    public LocalityDistrict save(LocalityDistrict localityDistrict) {
        AbstractLocality savedAbstractLocality = abstractLocalityRepository.save(AbstractLocality.fromLocalityDistrict(localityDistrict));
        return LocalityDistrict.fromAbstractLocality(savedAbstractLocality);
    }

    public List<LocalityDistrict> saveAll(List<LocalityDistrict> localityDistricts) {
        List<AbstractLocality> abstractLocalitiesToSave = localityDistricts.stream()
                .map(AbstractLocality::fromLocalityDistrict)
                .collect(Collectors.toList());
        return abstractLocalityRepository.saveAll(abstractLocalitiesToSave).stream()
                .map(LocalityDistrict::fromAbstractLocality)
                .collect(Collectors.toList());
    }

    public void deleteLocalityDistrict(LocalityDistrict localityDistrict) {
        AbstractLocality abstractLocalityToDelete = abstractLocalityRepository.findAbstractLocalityByCodeAndGenericLocalityType(localityDistrict.getCode(), GenericLocalityType.LOCALITY_DISTRICT)
                .orElseThrow(() -> new IllegalArgumentException(MessageFormat.format(LOCALITY_DISTRICT_WITH_CODE_DOESNT_EXIST, localityDistrict.getCode())));
        abstractLocalityRepository.delete(abstractLocalityToDelete);
    }

    List<LocalityDistrict> getAllLocalityDistricts() {
        return abstractLocalityRepository.findAllByGenericLocalityType(GenericLocalityType.LOCALITY_DISTRICT).stream()
                .map(LocalityDistrict::fromAbstractLocality)
                .collect(Collectors.toList());
    }

    List<LocalityDistrict> getAllLocalityDistrictsForLocality(Locality locality) {
        AbstractLocality parentLocality = AbstractLocality.fromLocality(locality);
        return abstractLocalityRepository.findAbstractLocalityByParentLocalityAndGenericLocalityType(parentLocality, GenericLocalityType.LOCALITY_DISTRICT)
                .stream()
                .map(LocalityDistrict::fromAbstractLocality)
                .collect(Collectors.toList());
    }

    LocalityDistrict getExistingLocalityDistrict(Long id) {
        return abstractLocalityRepository.findAbstractLocalityByIdAndGenericLocalityType(id, GenericLocalityType.LOCALITY_DISTRICT)
                .map(LocalityDistrict::fromAbstractLocality)
                .orElseThrow(() -> new IllegalArgumentException(MessageFormat.format(LOCALITY_DISTRICT_WITH_ID_DOESNT_EXIST, id)));
    }

}
