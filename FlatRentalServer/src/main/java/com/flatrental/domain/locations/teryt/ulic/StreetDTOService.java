package com.flatrental.domain.locations.teryt.ulic;

import com.flatrental.domain.settings.SettingsService;
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
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class StreetDTOService {

    @Autowired
    private SettingsService settingsService;

    @Autowired
    private ApplicationContext context;


    private static final String XML_EXTENSION = ".xml";
    private static final String XML_FILE_NOT_FOUND_MSG = "XML file with administration units was not found in catalog downloaded from TERYT";
    private static final String CATALOG_TAG = "catalog";
    private static final String INVALID_XML_MESSAGE = "Invalid xml structure. Catalog tag not present in xml file.";

    public List<StreetDTO> getAllStreetsFromUlicCatalog() throws IOException, ParserConfigurationException, SAXException, JAXBException {
        InputStream streetsXMLFile = getStreetsXMLFile();
        Document streetsDocument = createXMLDocument(streetsXMLFile);
        Node streetsCatalog = getCatalogNode(streetsDocument);
        JAXBContext jaxbContext = JAXBContext.newInstance(StreetList.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        StreetList streetList = (StreetList) jaxbUnmarshaller.unmarshal(streetsCatalog);
        return streetList.getStreetList();
    }

    private InputStream getStreetsXMLFile() throws IOException {
        ITerytWs1 terytClient = getTerytClient();
        XMLGregorianCalendar dateOfCurrentCatalogState = terytClient.pobierzDateAktualnegoKatUlic();
        PlikKatalog streetsCatalog = terytClient.pobierzKatalogULIC(dateOfCurrentCatalogState);
        JAXBElement<String> encodedCatalog = streetsCatalog.getPlikZawartosc();
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

}
