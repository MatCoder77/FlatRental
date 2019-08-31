package com.flatrental.domain.locations.teryt.terc;

import com.flatrental.domain.locations.commune.Commune;
import com.flatrental.domain.locations.commune.CommuneService;
import com.flatrental.domain.locations.district.District;
import com.flatrental.domain.locations.district.DistrictService;
import com.flatrental.domain.locations.voivodeship.Voivodeship;
import com.flatrental.domain.locations.voivodeship.VoivodeshipService;
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
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class AdministrationUnitUpdateService {

    @Autowired
    private SettingsService settingsService;

    @Autowired
    private AdministrationUnitService administrationUnitService;

    @Autowired
    private VoivodeshipService voivodeshipService;

    @Autowired
    private DistrictService districtService;

    @Autowired
    private CommuneService communeService;

    @Autowired
    private ApplicationContext context;


    private static final String XML_EXTENSION = ".xml";
    private static final String XML_FILE_NOT_FOUND_MSG = "XML file with administration unit changes was not found in catalog downloaded from TERYT";
    private static final String CATALOG_TAG = "zmiany";
    private static final String INVALID_XML_MESSAGE = "Invalid xml structure. Catalog tag not present in xml file.";


    public List<AdministrationUnitChangeDTO> getAllAdministrationUnitsChangesFromTercCatalog() throws IOException, ParserConfigurationException, SAXException, JAXBException, DatatypeConfigurationException, ParseException {
        InputStream administrationUnitChangesXMLFile = getAdministrationUnitChangesXMLFile();
        Document administrationUnitChangesDocument = createXMLDocument(administrationUnitChangesXMLFile);
        Node administrationUnitChangesCatalog = getCatalogNode(administrationUnitChangesDocument);
        JAXBContext jaxbContext = JAXBContext.newInstance(AdministrationUnitChangeList.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        AdministrationUnitChangeList administrationUnitChangeList = (AdministrationUnitChangeList) jaxbUnmarshaller.unmarshal(administrationUnitChangesCatalog);
        return administrationUnitChangeList.getAdministrationUnitChangeList();
    }

    private InputStream getAdministrationUnitChangesXMLFile() throws IOException, DatatypeConfigurationException, ParseException {
        ITerytWs1 terytClient = getTerytClient();
        XMLGregorianCalendar dateOfCurrentCatalogState = settingsService.getXMLGregorianCalendar("2008-01-01");//terytClient.pobierzDateAktualnegoKatTerc();
        XMLGregorianCalendar dateOfLocalCatalogState = settingsService.getXMLGregorianCalendar("2007-12-29");//settingsService.getTercLastUpdateDate();
        PlikZmiany administrationUnitsChangesCatalog = terytClient.pobierzZmianyTercUrzedowy(dateOfLocalCatalogState, dateOfCurrentCatalogState);
        JAXBElement<String> encodedCatalog = administrationUnitsChangesCatalog.getPlikZawartosc();
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

    public void performAdministrationUnitsAddition(List<AdministrationUnitChangeDTO> changeDTOs) {
        performVoivodeshipsAddition(changeDTOs);
        performDistrictsAddition(changeDTOs);
        performCommunesAddition(changeDTOs);
    }

    private void performVoivodeshipsAddition(List<AdministrationUnitChangeDTO> administrationUnitChangeDTOs) {
        administrationUnitChangeDTOs.stream()
                .filter(this::isAdditionChange)
                .map(AdministrationUnitChangeDTO::getAdministrationUnitAfterChange)
                .filter(administrationUnitService::isVoivodeshipUnit)
                .forEach(voivodeshipService::createVoivodeship);
    }

    private void performDistrictsAddition(List<AdministrationUnitChangeDTO> administrationUnitChangeDTOs) {
        administrationUnitChangeDTOs.stream()
                .filter(this::isAdditionChange)
                .map(AdministrationUnitChangeDTO::getAdministrationUnitAfterChange)
                .filter(administrationUnitService::isDistrictUnit)
                .forEach(districtService::createDistrict);
    }

    private void performCommunesAddition(List<AdministrationUnitChangeDTO> administrationUnitChangeDTOS) {
        administrationUnitChangeDTOS.stream()
                .filter(this::isAdditionChange)
                .map(AdministrationUnitChangeDTO::getAdministrationUnitAfterChange)
                .filter(administrationUnitService::isCommuneUnit)
                .forEach(communeService::createCommune);
    }

    private boolean isAdditionChange(AdministrationUnitChangeDTO changeDTO) {
        return changeDTO.getCorrectionType().equals(AdministrationUnitCorrectionType.ADDITION);
    }

    public List<Update<Voivodeship, AdministrationUnitDTO>> getVoivodeshipsUpdates(List<AdministrationUnitChangeDTO> changeDTOs) {
        return changeDTOs.stream()
                .filter(this::isModificationChange)
                .filter(changeDTO -> administrationUnitService.isVoivodeshipUnit(changeDTO.getAdministrationUnitBeforeChange()))
                .map(this::mapToVoivodeshipUpdate)
                .collect(Collectors.toList());
    }

    private Update<Voivodeship, AdministrationUnitDTO> mapToVoivodeshipUpdate(AdministrationUnitChangeDTO changeDTO) {
        Voivodeship voivodeshipToBeUpdated = voivodeshipService.getExistingVoivodeship(changeDTO.getVoivodeshipCodeBefore());
        AdministrationUnitDTO voivodeshipStateAfterChange = changeDTO.getAdministrationUnitAfterChange();
        return new Update<>(voivodeshipToBeUpdated, voivodeshipStateAfterChange);
    }

    public List<Update<District, AdministrationUnitDTO>> getDistrictUpdates(List<AdministrationUnitChangeDTO> changeDTOs) {
        return changeDTOs.stream()
                .filter(this::isModificationChange)
                .filter(changeDTO -> administrationUnitService.isDistrictUnit(changeDTO.getAdministrationUnitBeforeChange()))
                .map(this::mapToDistrictUpdate)
                .collect(Collectors.toList());
    }

    private Update<District, AdministrationUnitDTO> mapToDistrictUpdate(AdministrationUnitChangeDTO changeDTO) {
        AdministrationUnitDTO districtBeforeUpdate = changeDTO.getAdministrationUnitBeforeChange();
        District districtToBeUpdated = districtService.getExistingDistrict(districtBeforeUpdate.getDistrictCode(), districtBeforeUpdate.getVoivodeshipCode());
        AdministrationUnitDTO districtAfterUpdate = changeDTO.getAdministrationUnitAfterChange();
        return new Update<>(districtToBeUpdated, districtAfterUpdate);
    }

    public List<Update<Commune, AdministrationUnitDTO>> getCommuneUpdates(List<AdministrationUnitChangeDTO> changeDTOs) {
        return changeDTOs.stream()
                .filter(this::isModificationChange)
                .filter(changeDTO -> administrationUnitService.isCommuneUnit(changeDTO.getAdministrationUnitBeforeChange()))
                .map(this::mapToCommuneUpdate)
                .collect(Collectors.toList());
    }

    private Update<Commune, AdministrationUnitDTO> mapToCommuneUpdate(AdministrationUnitChangeDTO changeDTO) {
        AdministrationUnitDTO communeBeforeUpdate = changeDTO.getAdministrationUnitBeforeChange();
        Commune communeToBeUpdated = communeService.getExistingCommune(communeBeforeUpdate.getCommuneCode(), communeBeforeUpdate.getDistrictCode(), communeBeforeUpdate.getVoivodeshipCode());
        AdministrationUnitDTO communeAfterUpdate = changeDTO.getAdministrationUnitAfterChange();
        return new Update<>(communeToBeUpdated, communeAfterUpdate);
    }

    public void performAdministrationUnitUpdates(List<Update<Voivodeship, AdministrationUnitDTO>> voivodeshipUpdates,
                                                 List<Update<District, AdministrationUnitDTO>> districtUpdates,
                                                 List<Update<Commune, AdministrationUnitDTO>> communeUpdates) {
        performVoivodeshipUpdates(voivodeshipUpdates);
        performDistrictUpdates(districtUpdates);
        performCommuneUpdates(communeUpdates);
    }

    private void performVoivodeshipUpdates(List<Update<Voivodeship, AdministrationUnitDTO>> voivodeshipUpdates) {
        voivodeshipUpdates.stream()
                .forEach(voivodeshipService::updateVoivodeship);
    }

    private void performDistrictUpdates(List<Update<District, AdministrationUnitDTO>> districtUpdates) {
        districtUpdates.stream()
                .forEach(districtService::updateDistrict);
    }

    private void performCommuneUpdates(List<Update<Commune, AdministrationUnitDTO>> communeUpdates) {
        communeUpdates.stream()
                .forEach(communeService::updateCommune);
    }

    private boolean isModificationChange(AdministrationUnitChangeDTO changeDTO) {
        return changeDTO.getCorrectionType().equals(AdministrationUnitCorrectionType.MODIFICATION);
    }

    public List<Voivodeship> getVoivodeshipsToDelete(List<AdministrationUnitChangeDTO> changeDTOs) {
        return changeDTOs.stream()
                .filter(this::isDeletionChange)
                .map(AdministrationUnitChangeDTO::getAdministrationUnitBeforeChange)
                .filter(administrationUnitService::isVoivodeshipUnit)
                .map(AdministrationUnitDTO::getVoivodeshipCode)
                .map(voivodeshipService::getExistingVoivodeship)
                .collect(Collectors.toList());
    }

    public List<District> getDistrictsToDelete(List<AdministrationUnitChangeDTO> changeDTOs) {
        return changeDTOs.stream()
                .filter(this::isDeletionChange)
                .map(AdministrationUnitChangeDTO::getAdministrationUnitBeforeChange)
                .filter(administrationUnitService::isDistrictUnit)
                .map(districtService::getExistingDistrict)
                .collect(Collectors.toList());
    }

    public List<Commune> getCommunesToDelete(List<AdministrationUnitChangeDTO> changeDTOs) {
        return changeDTOs.stream()
                .filter(this::isDeletionChange)
                .map(AdministrationUnitChangeDTO::getAdministrationUnitBeforeChange)
                .filter(administrationUnitService::isCommuneUnit)
                .map(communeService::getExistingCommune)
                .collect(Collectors.toList());
    }
    public void performAdministrationUnitsDeletion(List<Voivodeship> voivodeshipsToDelete, List<District> districtsToDelete, List<Commune> communesToDelete) {
        performCommunesDeletion(communesToDelete);
        performDistrictsDeletion(districtsToDelete);
        performVoivodeshipsDeletion(voivodeshipsToDelete);
    }

    private void performCommunesDeletion(List<Commune> communesToDelete) {
        communesToDelete.stream()
                .forEach(communeService::deleteCommune);
    }

    private void performDistrictsDeletion(List<District> localityDistrictsToDelete) {
        localityDistrictsToDelete.stream()
                .forEach(districtService::deleteDistrict);
    }

    private void performVoivodeshipsDeletion(List<Voivodeship> voivodeshipsToDelete) {
        voivodeshipsToDelete.stream()
                .forEach(voivodeshipService::deleteVoivodeship);
    }

    private boolean isDeletionChange(AdministrationUnitChangeDTO changeDTO) {
        return changeDTO.getCorrectionType().equals(AdministrationUnitCorrectionType.DELETION);
    }

}
