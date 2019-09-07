package com.flatrental.domain.locations.teryt.terc;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class AdministrationUnitTypeAdapter extends XmlAdapter<String, AdministrationUnitTypeDTO> {

    @Override
    public AdministrationUnitTypeDTO unmarshal(String xml) {
        return AdministrationUnitTypeDTO.fromString(xml).orElse(null);
    }

    @Override
    public String marshal(AdministrationUnitTypeDTO administrationUnitTypeDTO) {
        return administrationUnitTypeDTO.getTypeValue();
    }
}
