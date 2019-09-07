package com.flatrental.domain.locations.teryt.terc;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class AdministrationUnitCorrectionTypeAdapter extends XmlAdapter<String, AdministrationUnitCorrectionType> {


    @Override
    public AdministrationUnitCorrectionType unmarshal(String v) {
        return AdministrationUnitCorrectionType.fromString(v);
    }

    @Override
    public String marshal(AdministrationUnitCorrectionType v) {
        return v.getSymbol();
    }

}
