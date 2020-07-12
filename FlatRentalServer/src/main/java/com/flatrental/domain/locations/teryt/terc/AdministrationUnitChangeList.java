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
    List<AdministrationUnitChangeDTO> administrationUnitChanges;

    public List<AdministrationUnitChangeDTO> getAdministrationUnitChanges() {
        return Optional.ofNullable(administrationUnitChanges)
                .orElse(Collections.emptyList());
    }

}
