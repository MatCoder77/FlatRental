package com.flatrental.teryt_api.payloads.ulic;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Optional;

public class StreetNameAdapter extends XmlAdapter<String, Optional<String>> {

    @Override
    public Optional<String> unmarshal(String v) {
        if (v.trim().isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(v);
    }

    @Override
    public String marshal(Optional<String> v) {
        return v.orElse("");
    }

}
