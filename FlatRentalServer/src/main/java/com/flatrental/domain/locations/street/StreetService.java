package com.flatrental.domain.locations.street;

import com.google.common.collect.Maps;
import com.flatrental.domain.locations.abstractlocality.AbstractLocality;
import com.flatrental.domain.locations.abstractlocality.AbstractLocalityService;
import com.flatrental.domain.locations.locality.Locality;
import com.flatrental.domain.locations.localitydistrict.LocalityDistrict;
import com.flatrental.domain.locations.localitypart.LocalityPart;
import com.flatrental.domain.locations.teryt.ulic.StreetDTO;
import com.flatrental.domain.locations.teryt.ulic.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class StreetService {

    @Autowired
    private StreetRepository streetRepository;

    @Autowired
    private AbstractLocalityService abstractLocalityService;

    private static final String STREET_NOT_FOUND = "Not found street with code {0}";


    public List<Street> createStreets(List<StreetDTO> streetDTOs,
                                      Map<String, Locality> autonomousLocalityByCode,
                                      Map<String, LocalityDistrict> localityDistrictByCode,
                                      Map<String, LocalityPart> localityPartByCode) {

        List<Street> uniqueStreets = streetDTOs.stream()
                .filter(distinctBy(StreetDTO::getStreetCode))
                .map(this::mapToStreet)
                .collect(Collectors.toList());

        streetRepository.saveAll(uniqueStreets);

        Map<String, Street> uniqueStreetByCode = uniqueStreets.stream()
                .collect(Collectors.toMap(Street::getCode, Function.identity()));

        Map<String, AbstractLocality> abstractLocalitiesByCode = Stream.of(
                autonomousLocalityByCode.entrySet()
                        .stream()
                        .map(entry -> Maps.immutableEntry(entry.getKey(), AbstractLocality.fromLocality(entry.getValue()))),
                localityDistrictByCode.entrySet()
                        .stream()
                        .map(entry -> Maps.immutableEntry(entry.getKey(), AbstractLocality.fromLocalityDistrict(entry.getValue()))),
                localityPartByCode.entrySet()
                        .stream()
                        .map(entry -> Maps.immutableEntry(entry.getKey(), AbstractLocality.fromLocalityPart(entry.getValue()))))
                .flatMap(Function.identity())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        streetDTOs.stream()
                .forEach(streetDTO -> abstractLocalityService.addStreet(abstractLocalitiesByCode.get(streetDTO.getDirectParentCode()), uniqueStreetByCode.get(streetDTO.getStreetCode())));

        return uniqueStreets;
    }

    private static <T> Predicate<T> distinctBy(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    private Street mapToStreet(StreetDTO streetDTO) {
        return new Street(streetDTO.getStreetCode(), streetDTO.getStreetType(), streetDTO.getMainName(), streetDTO.getLeadingName().orElse(null));
    }

    public void addStreetToAbstractLocality(StreetDTO streetDTO) {
        AbstractLocality abstractLocality = abstractLocalityService.getExistingAbstractLocalityByCode(streetDTO.getDirectParentCode());
        Street street = getExistingOrCreate(streetDTO);
        abstractLocalityService.addStreet(abstractLocality, street);
    }

    private Street getExistingOrCreate(StreetDTO streetDTO) {
        return streetRepository.findStreetByCode(streetDTO.getStreetCode())
                .orElseGet(() -> createStreet(streetDTO));
    }

    private Street createStreet(StreetDTO streetDTO) {
        Street street = mapToStreet(streetDTO);
        return streetRepository.save(street);
    }

    public Street getExistingStreetByCode(String code) {
        return streetRepository.findStreetByCode(code)
                .orElseThrow(() -> new IllegalArgumentException(MessageFormat.format(STREET_NOT_FOUND, code)));
    }

    public void updateStreet(Update<StreetAndAssociatedLocality, StreetDTO> update) {
        StreetAndAssociatedLocality streetAndAssociatedLocalityBeforeUpdate = update.getEntityBeforeUpdate();
        Street street = streetAndAssociatedLocalityBeforeUpdate.getStreet();
        AbstractLocality abstractLocalityBefore = streetAndAssociatedLocalityBeforeUpdate.getAbstractLocality();
        StreetDTO streetDTOAfterChange = update.getStateAfterUpdate();
        Street streetAfter = getExistingOrCreate(streetDTOAfterChange);
        abstractLocalityService.removeStreet(abstractLocalityBefore, street);
        AbstractLocality abstractLocalityAfter = abstractLocalityService.getExistingAbstractLocalityByCode(streetDTOAfterChange.getDirectParentCode());
        abstractLocalityService.addStreet(abstractLocalityAfter, streetAfter);
    }

    public void deleteStreet(StreetAndAssociatedLocality streetAndAssociatedLocality) {
        Street street = streetAndAssociatedLocality.getStreet();
        AbstractLocality abstractLocality = streetAndAssociatedLocality.getAbstractLocality();
        abstractLocalityService.removeStreet(abstractLocality, street);
    }

    public List<Street> getAllStreets() {
        return streetRepository.findAll();
    }

}
