package com.flatrental.domain.locations.locality;

import com.flatrental.domain.locations.abstractlocality.AbstractLocality;
import com.flatrental.domain.locations.abstractlocality.AbstractLocalityRepository;
import com.flatrental.domain.locations.abstractlocality.GenericLocalityType;
import com.flatrental.domain.locations.commune.Commune;
import com.flatrental.domain.locations.localitydistrict.LocalityDistrict;
import com.flatrental.domain.locations.localitypart.LocalityPart;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class LocalityRepository {

    private final AbstractLocalityRepository abstractLocalityRepository;

    private static final String LOCALITY_WITH_CODE_DOESNT_EXIST = "Locality with code {0} doesn't exist";

    Optional<Locality> findLocalityByCode(String code) {
        return abstractLocalityRepository.findAbstractLocalityByCodeAndGenericLocalityType(code, GenericLocalityType.AUTONOMOUS_LOCALITY)
                .map(Locality::fromAbstractLocality);
    }

    List<Locality> findLocalityByCommuneDistrictCode(String districtCode) {
        return abstractLocalityRepository.findAbstractLocalityByCommuneDistrictCodeAndGenericLocalityType(districtCode, GenericLocalityType.AUTONOMOUS_LOCALITY)
                .stream()
                .map(Locality::fromAbstractLocality)
                .collect(Collectors.toList());
    }

    Locality save(Locality locality) {
        AbstractLocality savedAbstractLocality = abstractLocalityRepository.save(AbstractLocality.fromLocality(locality));
        return Locality.fromAbstractLocality(savedAbstractLocality);
    }

    List<Locality> saveAll(Collection<Locality> localities) {
        List<AbstractLocality> localitiesToSave = localities.stream()
                .map(AbstractLocality::fromLocality)
                .collect(Collectors.toList());
        return abstractLocalityRepository.saveAll(localitiesToSave).stream()
                .map(Locality::fromAbstractLocality)
                .collect(Collectors.toList());
    }

    void deleteLocality(Locality locality) {
        AbstractLocality foundLocality = abstractLocalityRepository.findAbstractLocalityByCodeAndGenericLocalityType(locality.getCode(), GenericLocalityType.AUTONOMOUS_LOCALITY)
                .orElseThrow(() -> new IllegalArgumentException(MessageFormat.format(LOCALITY_WITH_CODE_DOESNT_EXIST, locality.getCode())));
        abstractLocalityRepository.delete(foundLocality);
    }

    List<Locality> getAllLocalities() {
        return abstractLocalityRepository.findAllByGenericLocalityType(GenericLocalityType.AUTONOMOUS_LOCALITY).stream()
                .map(Locality::fromAbstractLocality)
                .collect(Collectors.toList());
    }

    List<LocalityDistrict> getLocalityDistrictsForLocality(Locality locality) {
        return abstractLocalityRepository.findAbstractLocalityByParentLocalityAndGenericLocalityType(AbstractLocality.fromLocality(locality), GenericLocalityType.LOCALITY_DISTRICT)
                .stream()
                .map(LocalityDistrict::fromAbstractLocality)
                .collect(Collectors.toList());
    }

    List<LocalityPart> getLocalityPartsForLocality(Locality locality) {
        return abstractLocalityRepository.findAbstractLocalityByParentLocalityAndGenericLocalityType(AbstractLocality.fromLocality(locality), GenericLocalityType.LOCALITY_PART)
                .stream()
                .map(LocalityPart::fromAbstractLocality)
                .collect(Collectors.toList());
    }

    List<Locality> getLocalitiesForCommune(Commune commune) {
        return abstractLocalityRepository.findAbstractLocalityByCommuneAndGenericLocalityType(commune, GenericLocalityType.AUTONOMOUS_LOCALITY)
                .stream()
                .map(Locality::fromAbstractLocality)
                .collect(Collectors.toList());
    }

    Optional<Locality> findLocalityById(Long id) {
        return abstractLocalityRepository.findAbstractLocalityByIdAndGenericLocalityType(id, GenericLocalityType.AUTONOMOUS_LOCALITY)
                .map(Locality::fromAbstractLocality);
    }

}
