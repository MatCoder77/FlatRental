package com.flatrental.domain.settings;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.tempuri.ITerytWs1;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

@Service
@RequiredArgsConstructor
public class SettingsService {

    private final SettingsRepository settingsRepository;
    private final ApplicationContext context;

    private static final String SETTING_NOT_FOUND = "Setting {0} is not present";
    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public XMLGregorianCalendar getTercCatalogDate() throws DatatypeConfigurationException, ParseException {
        String stringDate = getSetting(SettingName.TERC_CATALOG_DATE);
        return getXMLGregorianCalendar(stringDate);
    }

    public XMLGregorianCalendar getSimcCatalogDate() throws DatatypeConfigurationException, ParseException {
        String stringDate = getSetting(SettingName.SIMC_CATALOG_DATE);
        return getXMLGregorianCalendar(stringDate);
    }

    public XMLGregorianCalendar getUlicLastUpdateDate() throws DatatypeConfigurationException, ParseException {
        String stringDate = getSetting(SettingName.ULIC_CATALOG_DATE);
        return getXMLGregorianCalendar(stringDate);
    }

    public String getSetting(SettingName settingName) {
        return settingsRepository.findById(settingName)
                .map(Settings::getValue)
                .orElseThrow(() -> new IllegalStateException(MessageFormat.format(SETTING_NOT_FOUND, settingName)));
    }

    public XMLGregorianCalendar getXMLGregorianCalendar(String stringDate) throws ParseException, DatatypeConfigurationException {
        Date date = dateFormat.parse(stringDate);
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        return   DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
    }

    public void updateCatalogsDate() {
        XMLGregorianCalendar currentTercCatalogDate = getTerytClient().pobierzDateAktualnegoKatTerc();
        XMLGregorianCalendar currentSimcCaltalogDate = getTerytClient().pobierzDateAktualnegoKatSimc();
        XMLGregorianCalendar currentUlicCatalogDate = getTerytClient().pobierzDateAktualnegoKatUlic();
        settingsRepository.save(Settings.createSetting(SettingName.TERC_CATALOG_DATE, dateFormat.format(currentTercCatalogDate.toGregorianCalendar().getTime())));
        settingsRepository.save(Settings.createSetting(SettingName.SIMC_CATALOG_DATE, dateFormat.format(currentSimcCaltalogDate.toGregorianCalendar().getTime())));
        settingsRepository.save(Settings.createSetting(SettingName.ULIC_CATALOG_DATE, dateFormat.format(currentUlicCatalogDate.toGregorianCalendar().getTime())));
    }

    private ITerytWs1 getTerytClient() {
        return context.getBean(ITerytWs1.class);
    }

}
