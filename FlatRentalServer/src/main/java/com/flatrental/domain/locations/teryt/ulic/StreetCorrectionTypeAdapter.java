package com.flatrental.domain.locations.teryt.ulic;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class StreetCorrectionTypeAdapter extends XmlAdapter<String, StreetCorrectionType> {

    @Override
    public StreetCorrectionType unmarshal(String v) {
        return StreetCorrectionType.fromString(v);
    }

    @Override
    public String marshal(StreetCorrectionType v) {
        return v.getSymbol();
    }

}
