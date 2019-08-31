package com.flatrental.domain.locations.teryt.terc;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@XmlRootElement(name = "zmiany")
@XmlAccessorType(XmlAccessType.FIELD)
public class AdministrationUnitChangeList {

    @XmlElement(name = "zmiana")
    List<AdministrationUnitChangeDTO> administrationUnitChangeList;

    public List<AdministrationUnitChangeDTO> getAdministrationUnitChangeList() {
        return Optional.ofNullable(administrationUnitChangeList)
                .orElse(Collections.emptyList());
    }

}
