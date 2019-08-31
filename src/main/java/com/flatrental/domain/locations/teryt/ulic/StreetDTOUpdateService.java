package com.flatrental.domain.locations.teryt.ulic;

import com.flatrental.domain.locations.abstractlocality.AbstractLocality;
import com.flatrental.domain.locations.abstractlocality.AbstractLocalityService;
import com.flatrental.domain.locations.street.Street;
import com.flatrental.domain.locations.street.StreetAndAssociatedLocality;
import com.flatrental.domain.locations.street.StreetService;
import com.flatrental.domain.settings.SettingsService;
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
public class StreetDTOUpdateService {

    @Autowired
    private SettingsService settingsService;

    @Autowired
    private StreetService streetService;

    @Autowired
    private AbstractLocalityService abstractLocalityService;

    @Autowired
    private ApplicationContext context;

    private static final String XML_EXTENSION = ".xml";
    private static final String XML_FILE_NOT_FOUND_MSG = "XML file with administration unit changes was not found in catalog downloaded from TERYT";
    private static final String CATALOG_TAG = "zmiany";
    private static final String INVALID_XML_MESSAGE = "Invalid xml structure. Catalog tag not present in xml file.";


    public List<StreetChangeDTO> getAllStreetChangesFromUlicCatalog() throws IOException, ParserConfigurationException, SAXException, JAXBException, DatatypeConfigurationException, ParseException {
        InputStream streetChangesXMLFile = getStreetChangesXMLFile();
        Document streetChangesDocument = createXMLDocument(streetChangesXMLFile);
        Node streetChangesCatalog = getCatalogNode(streetChangesDocument);
        JAXBContext jaxbContext = JAXBContext.newInstance(StreetChangeList.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        StreetChangeList streetChangeList = (StreetChangeList) jaxbUnmarshaller.unmarshal(streetChangesCatalog);
        return streetChangeList.getStreetChangeList();
    }

    private InputStream getStreetChangesXMLFile() throws IOException, DatatypeConfigurationException, ParseException {
        ITerytWs1 terytClient = getTerytClient();
        XMLGregorianCalendar dateOfCurrentCatalogState = terytClient.pobierzDateAktualnegoKatUlic();
        XMLGregorianCalendar dateOfLocalCatalogState = settingsService.getUlicLastUpdateDate();
        PlikZmiany streetChangesCatalog = terytClient.pobierzZmianyUlicUrzedowy(dateOfLocalCatalogState, dateOfCurrentCatalogState);
        JAXBElement<String> encodedCatalog = streetChangesCatalog.getPlikZawartosc();
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

    public void performStreetsAddition(List<StreetChangeDTO> changeDTOs) {
        changeDTOs.stream()
                .filter(this::isAddition)
                .map(StreetChangeDTO::getStreetDTOAfterChange)
                .forEach(streetService::addStreetToAbstractLocality);
    }

    private boolean isAddition(StreetChangeDTO changeDTO) {
        return changeDTO.getCorrectionType() == StreetCorrectionType.ADDITION;
    }

    public List<Update<StreetAndAssociatedLocality, StreetDTO>> getStreetUpdates(List<StreetChangeDTO> changeDTOs) {
        return changeDTOs.stream()
                .filter(this::isModification)
                .map(this::mapToStreetUpdate)
                .collect(Collectors.toList());
    }

    private boolean isModification(StreetChangeDTO changeDTO) {
        return changeDTO.getCorrectionType() == StreetCorrectionType.MODIFICATION ||
                changeDTO.getCorrectionType() == StreetCorrectionType.NAME_MODIFICATION;
    }

    private Update<StreetAndAssociatedLocality, StreetDTO> mapToStreetUpdate(StreetChangeDTO changeDTO) {
        StreetAndAssociatedLocality streetAndAssociatedLocality = getStreetWithAssociatedAbstractLocality(changeDTO.getStreetDTOBeforeChange());
        StreetDTO streetStateAfterChange = changeDTO.getStreetDTOAfterChange();
        return new Update<>(streetAndAssociatedLocality, streetStateAfterChange);
    }

    private StreetAndAssociatedLocality getStreetWithAssociatedAbstractLocality(StreetDTO streetDTO) {
        Street street = streetService.getExistingStreetByCode(streetDTO.getStreetCode());
        AbstractLocality abstractLocality = abstractLocalityService.getExistingAbstractLocalityByCode(streetDTO.getDirectParentCode());
        return new StreetAndAssociatedLocality(street, abstractLocality);
    }

    public void performStreetUpdates(List<Update<StreetAndAssociatedLocality, StreetDTO>> streetUpdates) {
        streetUpdates.stream()
                .forEach(streetService::updateStreet);
    }

    public List<StreetAndAssociatedLocality> getStreetsToDelete(List<StreetChangeDTO> changeDTOs) {
        return changeDTOs.stream()
                .filter(this::isDeletion)
                .map(StreetChangeDTO::getStreetDTOBeforeChange)
                .map(this::getStreetWithAssociatedAbstractLocality)
                .collect(Collectors.toList());
    }

    private boolean isDeletion(StreetChangeDTO changeDTO) {
        return changeDTO.getCorrectionType() == StreetCorrectionType.DELETION;
    }

    public void performStreetsDeletion(List<StreetAndAssociatedLocality> streetsToDelete) {
        streetsToDelete.stream()
                .forEach(streetService::deleteStreet);
    }

}
