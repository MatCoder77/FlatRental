package com.flatrental.domain.locations.localitypart;


import com.flatrental.domain.locations.abstractlocality.AbstractLocality;
import com.flatrental.domain.locations.abstractlocality.AbstractLocalityRepository;
import com.flatrental.domain.locations.abstractlocality.GenericLocalityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class LocalityPartRepository {

    @Autowired
    private AbstractLocalityRepository abstractLocalityRepository;

    private static final String LOCALITY_PART_WITH_CODE_DOESNT_EXIST = "LocalityPart with code {0} doesn't exist";


    Optional<LocalityPart> findLocalityPartByCode(String code) {
        return abstractLocalityRepository.findAbstractLocalityByCodeAndGenericLocalityType(code, GenericLocalityType.LOCALITY_PART)
                .map(LocalityPart::fromAbstractLocality);
    }

    public LocalityPart save(LocalityPart localityPart) {
        AbstractLocality savedAbstractLocality = abstractLocalityRepository.save(AbstractLocality.fromLocalityPart(localityPart));
        return LocalityPart.fromAbstractLocality(savedAbstractLocality);
    }

    public List<LocalityPart> saveAll(List<LocalityPart> localityParts) {
        List<AbstractLocality> abstractLocalitiesToSave = localityParts.stream()
                .map(AbstractLocality::fromLocalityPart)
                .collect(Collectors.toList());
        return abstractLocalityRepository.saveAll(abstractLocalitiesToSave)
                .stream()
                .map(LocalityPart::fromAbstractLocality)
                .collect(Collectors.toList());
    }

    public void deleteLocalityPart(LocalityPart localityPart) {
        AbstractLocality abstractLocalityToDelete = abstractLocalityRepository.findAbstractLocalityByCodeAndGenericLocalityType(localityPart.getCode(), GenericLocalityType.LOCALITY_PART)
                .orElseThrow(() -> new IllegalArgumentException(MessageFormat.format(LOCALITY_PART_WITH_CODE_DOESNT_EXIST, localityPart.getCode())));
        abstractLocalityRepository.delete(abstractLocalityToDelete);
    }

    public List<LocalityPart> getAllLocalityParts() {
        return abstractLocalityRepository.findAllByGenericLocalityType(GenericLocalityType.LOCALITY_PART).stream()
                .map(LocalityPart::fromAbstractLocality)
                .collect(Collectors.toList());
    }

}
