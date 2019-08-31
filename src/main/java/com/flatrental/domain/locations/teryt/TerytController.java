package com.flatrental.domain.locations.teryt;


import com.flatrental.domain.locations.commune.Commune;
import com.flatrental.domain.locations.commune.CommuneService;
import com.flatrental.domain.locations.district.District;
import com.flatrental.domain.locations.district.DistrictService;
import com.flatrental.domain.locations.locality.Locality;
import com.flatrental.domain.locations.locality.LocalityService;
import com.flatrental.domain.locations.localitydistrict.LocalityDistrict;
import com.flatrental.domain.locations.localitydistrict.LocalityDistrictService;
import com.flatrental.domain.locations.localitypart.LocalityPart;
import com.flatrental.domain.locations.localitypart.LocalityPartService;
import com.flatrental.domain.locations.localitytype.LocalityType;
import com.flatrental.domain.locations.localitytype.LocalityTypeService;
import com.flatrental.domain.locations.street.Street;
import com.flatrental.domain.locations.street.StreetAndAssociatedLocality;
import com.flatrental.domain.locations.street.StreetService;
import com.flatrental.domain.locations.voivodeship.Voivodeship;
import com.flatrental.domain.locations.voivodeship.VoivodeshipService;
import com.flatrental.domain.settings.SettingsService;
import com.flatrental.domain.locations.teryt.simc.LocalityChangeDTO;
import com.flatrental.domain.locations.teryt.simc.LocalityDTO;
import com.flatrental.domain.locations.teryt.simc.LocalityDTOService;
import com.flatrental.domain.locations.teryt.simc.LocalityTypeDTO;
import com.flatrental.domain.locations.teryt.simc.LocalityUpdateService;
import com.flatrental.domain.locations.teryt.terc.AdministrationUnitChangeDTO;
import com.flatrental.domain.locations.teryt.terc.AdministrationUnitDTO;
import com.flatrental.domain.locations.teryt.terc.AdministrationUnitService;
import com.flatrental.domain.locations.teryt.terc.AdministrationUnitUpdateService;
import com.flatrental.domain.locations.teryt.ulic.StreetChangeDTO;
import com.flatrental.domain.locations.teryt.ulic.StreetDTO;
import com.flatrental.domain.locations.teryt.ulic.StreetDTOService;
import com.flatrental.domain.locations.teryt.ulic.StreetDTOUpdateService;
import com.flatrental.domain.locations.teryt.ulic.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/teryt")
public class TerytController {

    @Autowired
    private AdministrationUnitService administrationUnitService;

    @Autowired
    private VoivodeshipService voivodeshipService;

    @Autowired
    private DistrictService districtService;

    @Autowired
    private CommuneService communeService;

    @Autowired
    private LocalityDTOService localityDTOService;

    @Autowired
    private LocalityService localityService;

    @Autowired
    private LocalityTypeService localityTypeService;

    @Autowired
    private LocalityPartService localityPartService;

    @Autowired
    private LocalityDistrictService localityDistrictService;

    @Autowired
    private StreetDTOService streetDTOService;

    @Autowired
    private StreetService streetService;

    @Autowired
    private AdministrationUnitUpdateService administrationUnitUpdateService;

    @Autowired
    private SettingsService settingsService;

    @Autowired
    private LocalityUpdateService localityUpdateService;

    @Autowired
    private StreetDTOUpdateService streetDTOUpdateService;


    @PostMapping("/load")
    @Transactional
    public Boolean loadTeryt() throws IOException, ParserConfigurationException, JAXBException, SAXException {
        List<AdministrationUnitDTO> administrationUnitDTOs = administrationUnitService.getAllAdministrationUnitsFromTercCatalog();

        List<AdministrationUnitDTO> voivodeshipDTOs = administrationUnitService.getVoivodeships(administrationUnitDTOs);
        List<Voivodeship> voivodeships = voivodeshipService.createVoivodeships(voivodeshipDTOs);

        Map<String, Voivodeship> voivodeshipByCode = voivodeships.stream()
                .collect(Collectors.toMap(Voivodeship::getCode, Function.identity()));
        List<AdministrationUnitDTO> districtDTOs = administrationUnitService.getDistricts(administrationUnitDTOs);
        List<District> districts = districtService.createDistricts(districtDTOs, voivodeshipByCode);

        Map<String, Map<String,District>> districtByCodeGroupedByVoivodeshipCode = districts.stream()
                .collect(Collectors.groupingBy(this::getVoivodeshipCode, Collectors.toMap(District::getCode, Function.identity())));
        List<AdministrationUnitDTO> communeDTOs = administrationUnitService.getCommunes(administrationUnitDTOs);
        List<Commune> communes = communeService.createCommunes(communeDTOs, districtByCodeGroupedByVoivodeshipCode);

        List<LocalityTypeDTO> localityTypeDTOs = localityDTOService.getAllLocalityTypesFromWmrodzCatalog();
        List<LocalityType> localityTypes = localityTypeService.createLocalityTypes(localityTypeDTOs);

        Map<String, LocalityType> localityTypeByCode = localityTypes.stream()
                .collect(Collectors.toMap(LocalityType::getTypeCode, Function.identity()));
        Map<District, Map<String, Commune>> communesByDistrict = communes.stream()
                .collect(Collectors.groupingBy(Commune::getDistrict, Collectors.toMap(Commune::getCode, Function.identity())));
        List<LocalityDTO> localityDTOs = localityDTOService.getAllLocalitiesFromSimcCatalog();
        List<LocalityDTO> autonomousLocalitiesDTOs = localityDTOService.getAutonomousLocalities(localityDTOs);
        List<Locality> autonomousLocalities = localityService.createLocalities(autonomousLocalitiesDTOs, districtByCodeGroupedByVoivodeshipCode, communesByDistrict, localityTypeByCode);

        List<LocalityDTO> localityDistrictDTOs = localityDTOService.getLocalityDistricts(localityDTOs);
        List<LocalityDistrict> localityDistricts = localityDistrictService.createLocalityDistricts(localityDistrictDTOs, autonomousLocalities, districtByCodeGroupedByVoivodeshipCode, localityTypeByCode);

        List<LocalityDTO> localityPartDTOs = localityDTOService.getLocalityPartsByParentLocalityCode(localityDTOs);
        Map<String, Locality> autonomousLocalityByCode = autonomousLocalities.stream()
                .collect(Collectors.toMap(Locality::getCode, Function.identity()));
        Map<String, LocalityDistrict> localityDistrictByCode = localityDistricts.stream()
                .collect(Collectors.toMap(LocalityDistrict::getCode, Function.identity()));
        List<LocalityPart> localityParts = localityPartService.createLocalityParts(localityPartDTOs, autonomousLocalityByCode, localityDistrictByCode, localityTypeByCode);

        Map<String, LocalityPart> localityPartByCode = localityParts.stream()
                .collect(Collectors.toMap(LocalityPart::getCode, Function.identity()));
        List<StreetDTO> streetDTOs = streetDTOService.getAllStreetsFromUlicCatalog();
        List<Street> streets = streetService.createStreets(streetDTOs, autonomousLocalityByCode, localityDistrictByCode, localityPartByCode);

        settingsService.updateCatalogsDate();

        return true;
    }

    private String getVoivodeshipCode(District district) {
        return  district.getVoivodeship().getCode();
    }

    @PutMapping("/update")
    @Transactional
    public boolean updateTeryt() throws SAXException, ParseException, IOException, JAXBException, ParserConfigurationException, DatatypeConfigurationException {
        List<AdministrationUnitChangeDTO> administrationUnitChangeDTOs = administrationUnitUpdateService.getAllAdministrationUnitsChangesFromTercCatalog();
        List<LocalityChangeDTO> localityChangeDTOs = localityUpdateService.getAllLocalityChangesFromSimcCatalog();
        List<StreetChangeDTO> streetChangeDTOs = streetDTOUpdateService.getAllStreetChangesFromUlicCatalog();

        administrationUnitUpdateService.performAdministrationUnitsAddition(administrationUnitChangeDTOs);
        localityUpdateService.performLocalitiesAddition(localityChangeDTOs);
        streetDTOUpdateService.performStreetsAddition(streetChangeDTOs);

        List<Update<Voivodeship, AdministrationUnitDTO>> voivodeshipUpdates = administrationUnitUpdateService.getVoivodeshipsUpdates(administrationUnitChangeDTOs);
        List<Update<District, AdministrationUnitDTO>> districtUpdates = administrationUnitUpdateService.getDistrictUpdates(administrationUnitChangeDTOs);
        List<Update<Commune, AdministrationUnitDTO>> communeUpdates = administrationUnitUpdateService.getCommuneUpdates(administrationUnitChangeDTOs);
        List<Update<Locality, LocalityDTO>> autonomousLocalityUpdates = localityUpdateService.getAutonomousLocalityUpdates(localityChangeDTOs);
        List<Update<LocalityDistrict, LocalityDTO>> localityDistrictUpdates = localityUpdateService.getLocalityDistrictUpdates(localityChangeDTOs);
        List<Update<LocalityPart, LocalityDTO>> localityPartUpdates = localityUpdateService.getLocalityPartUpdates(localityChangeDTOs);
        List<Update<StreetAndAssociatedLocality, StreetDTO>> streetUpdates = streetDTOUpdateService.getStreetUpdates(streetChangeDTOs);

        List<Voivodeship> voivodeshipsToDelete = administrationUnitUpdateService.getVoivodeshipsToDelete(administrationUnitChangeDTOs);
        List<District> districtsToDelete = administrationUnitUpdateService.getDistrictsToDelete(administrationUnitChangeDTOs);
        List<Commune> communesToDelete = administrationUnitUpdateService.getCommunesToDelete(administrationUnitChangeDTOs);
        List<Locality> autonomousLocalitiesToDelete = localityUpdateService.getAutonomousLocalitiesToDelete(localityChangeDTOs);
        List<LocalityDistrict> localityDistrictsToDelete = localityUpdateService.getLocalityDistrictsToDelete(localityChangeDTOs);
        List<LocalityPart> localityPartsToDelete = localityUpdateService.getLocalityPartsToDelete(localityChangeDTOs);
        List<StreetAndAssociatedLocality> streetsToDelete = streetDTOUpdateService.getStreetsToDelete(streetChangeDTOs);

        administrationUnitUpdateService.performAdministrationUnitUpdates(voivodeshipUpdates, districtUpdates, communeUpdates);
        localityUpdateService.performLocalityUpdates(autonomousLocalityUpdates, localityDistrictUpdates, localityPartUpdates);
        streetDTOUpdateService.performStreetUpdates(streetUpdates);

        streetDTOUpdateService.performStreetsDeletion(streetsToDelete);
        localityUpdateService.performLocalitiesDeletion(autonomousLocalitiesToDelete, localityDistrictsToDelete, localityPartsToDelete);
        administrationUnitUpdateService.performAdministrationUnitsDeletion(voivodeshipsToDelete, districtsToDelete, communesToDelete);

        settingsService.updateCatalogsDate();

        return true;
    }

}