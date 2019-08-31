package com.flatrental.domain.locations.teryt.simc;

import com.flatrental.domain.locations.abstractlocality.AbstractLocality;
import com.flatrental.domain.locations.locality.Locality;
import com.flatrental.domain.locations.locality.LocalityService;
import com.flatrental.domain.locations.localitydistrict.LocalityDistrict;
import com.flatrental.domain.locations.localitydistrict.LocalityDistrictService;
import com.flatrental.domain.locations.localitypart.LocalityPart;
import com.flatrental.domain.locations.localitypart.LocalityPartService;
import com.flatrental.domain.settings.SettingsService;
import com.flatrental.domain.locations.teryt.ulic.Update;
import org.datacontract.schemas._2004._07.terytuslugaws1.PlikZmiany;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.tempuri.ITerytWs1;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class LocalityUpdateService {

    @Autowired
    private SettingsService settingsService;

    @Autowired
    private LocalityDTOService localityDTOService;

    @Autowired
    private LocalityService localityService;

    @Autowired
    private LocalityDistrictService localityDistrictService;

    @Autowired
    private LocalityPartService localityPartService;

    @Autowired
    private ApplicationContext context;


    private static final String XML_EXTENSION = ".xml";
    private static final String XML_FILE_NOT_FOUND_MSG = "XML file with localities changes was not found in catalog downloaded from SIMC";
    private static final String CATALOG_TAG = "zmiany";
    private static final String INVALID_XML_MESSAGE = "Invalid xml structure. Catalog tag not present in xml file.";


    public List<LocalityChangeDTO> getAllLocalityChangesFromSimcCatalog() throws IOException, ParserConfigurationException, SAXException, JAXBException, DatatypeConfigurationException, ParseException {
        InputStream localityChangesXMLFile = getLocalityChangesXMLFile();
        Document localityChangesDocument = createXMLDocument(localityChangesXMLFile);
        Node localityChangesCatalog = getCatalogNode(localityChangesDocument);
        JAXBContext jaxbContext = JAXBContext.newInstance(LocalityChangeList.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        LocalityChangeList localityChangeList = (LocalityChangeList) jaxbUnmarshaller.unmarshal(localityChangesCatalog);
        return localityChangeList.getLocalityChangeList();
    }

    private InputStream getLocalityChangesXMLFile() throws IOException, DatatypeConfigurationException, ParseException {
        ITerytWs1 terytClient = getTerytClient();
        XMLGregorianCalendar dateOfCurrentCatalogState = settingsService.getXMLGregorianCalendar("2008-01-01");//terytClient.pobierzDateAktualnegoKatTerc();
        XMLGregorianCalendar dateOfLocalCatalogState = settingsService.getXMLGregorianCalendar("2007-12-29");//settingsService.getTercLastUpdateDate();
        PlikZmiany localityChangesCatalog = terytClient.pobierzZmianySimcUrzedowy(dateOfLocalCatalogState, dateOfCurrentCatalogState);
        JAXBElement<String> encodedCatalog = localityChangesCatalog.getPlikZawartosc();
        byte[] rawData = Base64.getDecoder().decode(encodedCatalog.getValue());
        ZipInputStream zipStream = new ZipInputStream(new ByteArrayInputStream(rawData));
        ZipEntry entry;
        while((entry = zipStream.getNextEntry()) != null) {
            if(isXMLFile(entry.getName())) {
                return zipStream;
            }
        }
        throw new FileNotFoundException(XML_FILE_NOT_FOUND_MSG);
    }

    private ITerytWs1 getTerytClient() {
        return context.getBean(ITerytWs1.class);
    }

    private Document createXMLDocument(InputStream xmlFile) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        return builder.parse(xmlFile);
    }

    private Node getCatalogNode(Document administrationUnitsDocument) {
        return Optional.ofNullable(administrationUnitsDocument.getElementsByTagName(CATALOG_TAG).item(0))
                .orElseThrow(() -> new IllegalArgumentException(INVALID_XML_MESSAGE));
    }

    private boolean isXMLFile(String fileName) {
        return fileName.endsWith(XML_EXTENSION);
    }

    public void performLocalitiesAddition(List<LocalityChangeDTO> changeDTOs) {
        performAutonomousLocalitiesAddition(changeDTOs);
        performLocalityDistrictsAddition(changeDTOs);
        performLocalityPartsAddition(changeDTOs);
    }

    private void performAutonomousLocalitiesAddition(List<LocalityChangeDTO> changeDTOs) {
        changeDTOs.stream()
                .filter(this::isAdditionChange)
                .map(LocalityChangeDTO::getLocalityDTOAfterChange)
                .filter(localityDTOService::isAutonomousLocality)
                .forEach(localityService::createLocality);
    }

    private void performLocalityDistrictsAddition(List<LocalityChangeDTO> changeDTOs) {
        changeDTOs.stream()
                .filter(this::isAdditionChange)
                .map(LocalityChangeDTO::getLocalityDTOAfterChange)
                .filter(localityDTOService::isLocalityDistrict)
                .forEach(localityDistrictService::createLocalityDistrict);
    }

    private void performLocalityPartsAddition(List<LocalityChangeDTO> changeDTOs) {
        changeDTOs.stream()
                .filter(this::isAdditionChange)
                .map(LocalityChangeDTO::getLocalityDTOAfterChange)
                .filter(localityDTOService::isLocalityPart)
                .forEach(localityPartService::createLocalityPart);
    }

    private boolean isAdditionChange(LocalityChangeDTO changeDTO) {
        return changeDTO.getCorrectionType().equals(LocalityCorrectionType.ADDITION);
    }

    public List<Update<Locality, LocalityDTO>> getAutonomousLocalityUpdates(List<LocalityChangeDTO> changeDTOs) {
        return changeDTOs.stream()
                .filter(this::isModificationChange)
                .filter(changeDTO -> localityDTOService.isAutonomousLocality(changeDTO.getLocalityDTOBeforeChange()))
                .map(this::mapToAutonomousLocalityUpdate)
                .collect(Collectors.toList());
    }

    private Update<Locality, LocalityDTO> mapToAutonomousLocalityUpdate(LocalityChangeDTO changeDTO) {
        LocalityDTO localityBeforeUpdete = changeDTO.getLocalityDTOBeforeChange();
        Locality localityToBeUpdated = localityService.getExistingLocality(localityBeforeUpdete.getLocalityCode());
        LocalityDTO localityAfterUpdate = changeDTO.getLocalityDTOAfterChange();
        return new Update<>(localityToBeUpdated, localityAfterUpdate);
    }

    public List<Update<LocalityDistrict, LocalityDTO>> getLocalityDistrictUpdates(List<LocalityChangeDTO> changeDTOs) {
        return changeDTOs.stream()
                .filter(this::isModificationChange)
                .filter(changeDTO -> localityDTOService.isLocalityDistrict(changeDTO.getLocalityDTOBeforeChange()))
                .map(this::mapToLocalityDistrictUpdate)
                .collect(Collectors.toList());
    }

    private Update<LocalityDistrict, LocalityDTO> mapToLocalityDistrictUpdate(LocalityChangeDTO changeDTO) {
        LocalityDTO localityDistrictBeforeUpdate = changeDTO.getLocalityDTOBeforeChange();
        LocalityDistrict localityDistrictToBeUpdated = localityDistrictService.getExistingLocalityDistrict(localityDistrictBeforeUpdate.getLocalityCode());
        LocalityDTO localityDistrictAfterUpdate = changeDTO.getLocalityDTOAfterChange();
        return new Update<>(localityDistrictToBeUpdated, localityDistrictAfterUpdate);
    }

    public List<Update<LocalityPart, LocalityDTO>> getLocalityPartUpdates(List<LocalityChangeDTO> changeDTOs) {
        return changeDTOs.stream()
                .filter(this::isModificationChange)
                .filter(changeDTO -> localityDTOService.isLocalityPart(changeDTO.getLocalityDTOBeforeChange()))
                .map(this::mapToLocalityPartUpdate)
                .collect(Collectors.toList());
    }

    private Update<LocalityPart, LocalityDTO> mapToLocalityPartUpdate(LocalityChangeDTO changeDTO) {
        LocalityDTO localityPartBeforeUpdate = changeDTO.getLocalityDTOBeforeChange();
        LocalityPart localityPartToBeUpdated = localityPartService.getExistingLocalityPart(localityPartBeforeUpdate.getLocalityCode());
        LocalityDTO localityPartAfterUpdate = changeDTO.getLocalityDTOAfterChange();
        return new Update<>(localityPartToBeUpdated, localityPartAfterUpdate);
    }

    public void performLocalityUpdates(List<Update<Locality, LocalityDTO>> autonomousLocalityUpdates,
                                       List<Update<LocalityDistrict, LocalityDTO>> localityDistrictUpdates,
                                       List<Update<LocalityPart, LocalityDTO>> localityPartUpdates) {

        performForLocalitiesThatWillBeAutonomousLocalityAfterUpdate(autonomousLocalityUpdates, localityDistrictUpdates, localityPartUpdates);
        performForLocalitiesThatWillBeLocalityDistrictAfterUpdate(autonomousLocalityUpdates, localityDistrictUpdates, localityPartUpdates);
        performForLocalitiesThatWillBeLocalityPartAfterUpdate(autonomousLocalityUpdates, localityDistrictUpdates, localityPartUpdates);
    }

    private void performForLocalitiesThatWillBeAutonomousLocalityAfterUpdate(List<Update<Locality, LocalityDTO>> autonomousLocalityUpdates,
                                                                             List<Update<LocalityDistrict, LocalityDTO>> localityDistrictUpdates,
                                                                             List<Update<LocalityPart, LocalityDTO>> localityPartUpdates) {

        Predicate<LocalityDTO> isAutonomousLocalityPredicate = localityDTO -> localityDTOService.isAutonomousLocality(localityDTO);
        performForLocalitiesWithPredicate(autonomousLocalityUpdates, localityDistrictUpdates, localityPartUpdates, isAutonomousLocalityPredicate);
    }

    private void performForLocalitiesThatWillBeLocalityDistrictAfterUpdate(List<Update<Locality, LocalityDTO>> autonomousLocalityUpdates,
                                                                           List<Update<LocalityDistrict, LocalityDTO>> localityDistrictUpdates,
                                                                           List<Update<LocalityPart, LocalityDTO>> localityPartUpdates) {

        Predicate<LocalityDTO> isLocalityDistrictPredicate = localityDTO -> localityDTOService.isLocalityDistrict(localityDTO);
        performForLocalitiesWithPredicate(autonomousLocalityUpdates, localityDistrictUpdates, localityPartUpdates, isLocalityDistrictPredicate);
    }

    private void performForLocalitiesThatWillBeLocalityPartAfterUpdate(List<Update<Locality, LocalityDTO>> autonomousLocalityUpdates,
                                                                       List<Update<LocalityDistrict, LocalityDTO>> localityDistrictUpdates,
                                                                       List<Update<LocalityPart, LocalityDTO>> localityPartUpdates) {

        Predicate<LocalityDTO> isLocalityPartPredicate = localityDTO -> localityDTOService.isLocalityPart(localityDTO);
        performForLocalitiesWithPredicate(autonomousLocalityUpdates, localityDistrictUpdates, localityPartUpdates, isLocalityPartPredicate);
    }

    private void performForLocalitiesWithPredicate(List<Update<Locality, LocalityDTO>> autonomousLocalityUpdates,
                                                   List<Update<LocalityDistrict, LocalityDTO>> localityDistrictUpdates,
                                                   List<Update<LocalityPart, LocalityDTO>> localityPartUpdates,
                                                   Predicate<LocalityDTO> condition) {
        autonomousLocalityUpdates.stream()
                .filter(update -> condition.test(update.getStateAfterUpdate()))
                .map(update -> new Update(AbstractLocality.fromLocality(update.getEntityBeforeUpdate()), update.getStateAfterUpdate()))
                .forEach(localityService::updateLocality);
        localityDistrictUpdates.stream()
                .filter(update -> condition.test(update.getStateAfterUpdate()))
                .map(update -> new Update(AbstractLocality.fromLocalityDistrict(update.getEntityBeforeUpdate()), update.getStateAfterUpdate()))
                .forEach(localityDistrictService::updateLocalityDistrict);
        localityPartUpdates.stream()
                .filter(update -> condition.test(update.getStateAfterUpdate()))
                .map(update -> new Update(AbstractLocality.fromLocalityPart(update.getEntityBeforeUpdate()), update.getStateAfterUpdate()))
                .forEach(localityPartService::updateLocalityPart);
    }

    private boolean isModificationChange(LocalityChangeDTO changeDTO) {
        return changeDTO.getCorrectionType().equals(LocalityCorrectionType.EDITION) ||
                changeDTO.getCorrectionType().equals(LocalityCorrectionType.MOVE);
    }

    public List<Locality> getAutonomousLocalitiesToDelete(List<LocalityChangeDTO> changeDTOs) {
        return changeDTOs.stream()
                .filter(this::isDeletionChange)
                .map(LocalityChangeDTO::getLocalityDTOBeforeChange)
                .filter(localityDTOService::isAutonomousLocality)
                .map(LocalityDTO::getLocalityCode)
                .map(localityService::getExistingLocality)
                .collect(Collectors.toList());
    }

    public List<LocalityDistrict> getLocalityDistrictsToDelete(List<LocalityChangeDTO> changeDTOs) {
        return changeDTOs.stream()
                .filter(this::isDeletionChange)
                .map(LocalityChangeDTO::getLocalityDTOBeforeChange)
                .filter(localityDTOService::isLocalityDistrict)
                .map(LocalityDTO::getLocalityCode)
                .map(localityDistrictService::getExistingLocalityDistrict)
                .collect(Collectors.toList());
    }

    public List<LocalityPart> getLocalityPartsToDelete(List<LocalityChangeDTO> changeDTOs) {
        return changeDTOs.stream()
                .filter(this::isDeletionChange)
                .map(LocalityChangeDTO::getLocalityDTOBeforeChange)
                .filter(localityDTOService::isLocalityPart)
                .map(LocalityDTO::getLocalityCode)
                .map(localityPartService::getExistingLocalityPart)
                .collect(Collectors.toList());
    }

    public void performLocalitiesDeletion(List<Locality> localitiesToDelete, List<LocalityDistrict> localityDistrictsToDelete, List<LocalityPart> localityPartsToDelete) {
        performLocalityPartsDeletion(localityPartsToDelete);
        performLocalityDistrictsDeletion(localityDistrictsToDelete);
        performAutonomousLocalitiesDeletion(localitiesToDelete);
    }

    private void performAutonomousLocalitiesDeletion(List<Locality> localitiesToDelete) {
        localitiesToDelete.stream()
                .forEach(localityService::deleteLocality);
    }

    private void performLocalityDistrictsDeletion(List<LocalityDistrict> localityDistrictsToDelete) {
        localityDistrictsToDelete.stream()
                .forEach(localityDistrictService::deleteLocalityDistrict);
    }

    private void performLocalityPartsDeletion(List<LocalityPart> localityPartsToDelete) {
        localityPartsToDelete.stream()
                .forEach(localityPartService::deleteLocalityPart);
    }

    private boolean isDeletionChange(LocalityChangeDTO changeDTO) {
        return changeDTO.getCorrectionType().equals(LocalityCorrectionType.DELETION);
    }

}
