package com.flatrental.domain.locations.teryt.terc;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class AdministrationUnitTypeCodeAdapter extends XmlAdapter<String, AdministrationUnitTypeCode> {

    @Override
    public AdministrationUnitTypeCode unmarshal(String xml) {
        return AdministrationUnitTypeCode.fromString(xml).orElse(null);
    }

    @Override
    public String marshal(AdministrationUnitTypeCode administrationUnitTypeCode) {
        return String.valueOf(administrationUnitTypeCode.getCode());
    }

}
