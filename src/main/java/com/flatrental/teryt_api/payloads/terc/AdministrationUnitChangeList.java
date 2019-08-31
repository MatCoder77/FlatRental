package com.flatrental.teryt_api.payloads.terc;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "zmiany")
@XmlAccessorType(XmlAccessType.FIELD)
public class AdministrationUnitChangeList {

    @XmlElement(name = "zmiana")
    List<AdministrationUnitChangeDTO> administrationUnitChangeList;

    public List<AdministrationUnitChangeDTO> getAdministrationUnitChangeList() {
        return administrationUnitChangeList;
    }

}
