package com.flatrental.domain.locations.abstractlocality;

import com.flatrental.domain.locations.street.Street;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
public class AbstractLocalityService {

    @Autowired
    private AbstractLocalityRepository abstractLocalityRepository;

    private static final String NO_ABSTRACT_LOCALITY_WITH_SUPPLIED_CODE = "There is no AbstractLocality with code {0}";

    public AbstractLocality getExistingAbstractLocalityByCode(String code) {
        return abstractLocalityRepository.findAbstractLocalityByCode(code)
                .orElseThrow(() -> new IllegalArgumentException(MessageFormat.format(NO_ABSTRACT_LOCALITY_WITH_SUPPLIED_CODE, code)));
    }

    public void addStreet(AbstractLocality abstractLocality, Street street) {
        abstractLocality.addStreet(street);
        abstractLocalityRepository.save(abstractLocality);
    }

    public void removeStreet(AbstractLocality abstractLocality, Street street) {
        abstractLocality.removeStreet(street);
        abstractLocalityRepository.save(abstractLocality);
    }

    public AbstractLocality save(AbstractLocality abstractLocality) {
        return abstractLocalityRepository.save(abstractLocality);
    }

}
