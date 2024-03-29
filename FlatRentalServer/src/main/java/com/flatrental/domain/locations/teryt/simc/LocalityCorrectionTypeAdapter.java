package com.flatrental.domain.locations.teryt.simc;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class LocalityCorrectionTypeAdapter extends XmlAdapter<String, LocalityCorrectionType> {
    @Override
    public LocalityCorrectionType unmarshal(String v) {
        return LocalityCorrectionType.fromString(v);
    }

    @Override
    public String marshal(LocalityCorrectionType v) {
        return v.getSymbol();
    }

}
