package com.flatrental.teryt_api.payloads.ulic;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class StreetTypeAdapter extends XmlAdapter<String, StreetType> {

    @Override
    public StreetType unmarshal(String v) {
        return StreetType.fromString(v);
    }

    @Override
    public String marshal(StreetType v) {
        return v.getReadableValue();
    }

}
