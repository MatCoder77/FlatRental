package com.flatrental.teryt_api.payloads.terc;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Optional;

public class SetNullIfBlankStringAdapter extends XmlAdapter<String, String> {

    @Override
    public String unmarshal(String v) {
        if (v == null || v.trim().isEmpty()) {
            return null;
        }
        return v;
    }

    @Override
    public String marshal(String v) {
        return Optional.ofNullable(v).orElse("");
    }
}
