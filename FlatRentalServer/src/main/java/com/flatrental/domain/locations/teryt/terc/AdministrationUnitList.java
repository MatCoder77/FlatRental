package com.flatrental.domain.locations.teryt.terc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@XmlRootElement(name = "catalog")
@XmlAccessorType(XmlAccessType.FIELD)
public class AdministrationUnitList {

    @XmlElement(name = "row")
    private List<AdministrationUnitDTO> administrationUnitDTOList;

    public List<AdministrationUnitDTO> getAdministrationUnitDTOList() {
        return Optional.ofNullable(administrationUnitDTOList)
                .orElse(Collections.emptyList());
    }

    public void setAdministrationUnitDTOList(List<AdministrationUnitDTO> administrationUnitDTOList) {
        this.administrationUnitDTOList = administrationUnitDTOList;
    }
}
