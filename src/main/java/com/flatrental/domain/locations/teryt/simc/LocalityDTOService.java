package com.flatrental.domain.locations.teryt.simc;

import com.flatrental.domain.settings.SettingsService;
import com.flatrental.domain.locations.teryt.terc.AdministrationUnitTypeCode;
import org.datacontract.schemas._2004._07.terytuslugaws1.PlikKatalog;
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
public class LocalityDTOService {

    @Autowired
    private SettingsService settingsService;

    @Autowired
    private ApplicationContext context;


    private static final String XML_EXTENSION = ".xml";
    private static final String XML_FILE_NOT_FOUND_MSG = "XML file with administration units was not found in catalog downloaded from TERYT";
    private static final String CATALOG_TAG = "catalog";
    private static final String INVALID_XML_MESSAGE = "Invalid xml structure. Catalog tag not present in xml file.";

    public List<LocalityDTO> getAllLocalitiesFromSimcCatalog() throws IOException, ParserConfigurationException, SAXException, JAXBException {
        InputStream localitiesXMLFile = getLocalitiesXMLFile();
        Document localitiesDocument = createXMLDocument(localitiesXMLFile);
        Node localitiesCatalog = getCatalogNode(localitiesDocument);
        JAXBContext jaxbContext = JAXBContext.newInstance(LocalityList.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        LocalityList localityList = (LocalityList) jaxbUnmarshaller.unmarshal(localitiesCatalog);
        return localityList.getLocalityList();
    }

    private InputStream getLocalitiesXMLFile() throws IOException {
        ITerytWs1 terytClient = getTerytClient();
        XMLGregorianCalendar dateOfCurrentCatalogState = null;//terytClient.pobierzDateAktualnegoKatTerc();
        try {
            dateOfCurrentCatalogState = settingsService.getXMLGregorianCalendar("2007-12-29");
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }
        PlikKatalog localitiesCatalog = terytClient.pobierzKatalogSIMC(dateOfCurrentCatalogState);
        JAXBElement<String> encodedCatalog = localitiesCatalog.getPlikZawartosc();
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

    public List<LocalityTypeDTO> getAllLocalityTypesFromWmrodzCatalog() throws JAXBException, IOException, SAXException, ParserConfigurationException {
        InputStream localityTypesXMLFile = getLocalityTypesXMLFile();
        Document localityTypesDocument = createXMLDocument(localityTypesXMLFile);
        Node localityTypesCatalog = getCatalogNode(localityTypesDocument);
        JAXBContext jaxbContext = JAXBContext.newInstance(LocalityTypeList.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        LocalityTypeList localityTypeList = (LocalityTypeList) jaxbUnmarshaller.unmarshal(localityTypesCatalog);
        return localityTypeList.getLocalityTypeList();
    }

    private InputStream getLocalityTypesXMLFile() throws IOException {
        ITerytWs1 terytClient = getTerytClient();
        XMLGregorianCalendar dateOfCurrentCatalogState = null;//terytClient.pobierzDateAktualnegoKatTerc();
        try {
            dateOfCurrentCatalogState = settingsService.getXMLGregorianCalendar("2007-12-29");
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }
        PlikKatalog localityTypesCatalog = terytClient.pobierzKatalogWMRODZ(dateOfCurrentCatalogState);
        JAXBElement<String> encodedCatalog = localityTypesCatalog.getPlikZawartosc();
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

    public List<LocalityDTO> getAutonomousLocalities(List<LocalityDTO> localityDTOs) {
        return localityDTOs.stream()
                .filter(this::isAutonomousLocality)
                .collect(Collectors.toList());
    }

    public boolean isAutonomousLocality(LocalityDTO localityDTO) {
        return  localityCodeAndParentLocalityCodeAreEquals(localityDTO) &&
                !isDistrictOfCity(localityDTO) &&
                !isDistrictOfCapital(localityDTO);
    }

    private boolean localityCodeAndParentLocalityCodeAreEquals(LocalityDTO localityDTO) {
        return localityDTO.getLocalityCode().equals(localityDTO.getDirectParentCode());
    }

    private boolean isDistrictOfCapital(LocalityDTO localityDTO) {
        return localityDTO.getUnitTypeCode().equals(AdministrationUnitTypeCode.DISTRICT_OF_CAPITAL);
    }

    private boolean isDistrictOfCity(LocalityDTO localityDTO) {
        return localityDTO.getUnitTypeCode().equals(AdministrationUnitTypeCode.DISTRICT_OF_CITY);
    }

    public boolean isLocalityDistrict(LocalityDTO localityDTO) {
        return (isDistrictOfCapital(localityDTO) || isDistrictOfCity(localityDTO)) &&
                localityCodeAndParentLocalityCodeAreEquals(localityDTO);
    }

    public List<LocalityDTO> getLocalityDistricts(List<LocalityDTO> localityDTOs) {
        return localityDTOs.stream()
                .filter(this::isLocalityDistrict)
                .collect(Collectors.toList());
    }

    public List<LocalityDTO> getLocalityPartsByParentLocalityCode(List<LocalityDTO> localityDTOs) {
        return localityDTOs.stream()
                .filter(this::isLocalityPart)
                .collect(Collectors.toList());
    }

    public boolean isLocalityPart(LocalityDTO localityDTO) {
        return !localityCodeAndParentLocalityCodeAreEquals(localityDTO);
    }

}
