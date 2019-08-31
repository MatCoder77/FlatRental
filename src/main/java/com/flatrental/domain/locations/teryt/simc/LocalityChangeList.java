package com.flatrental.domain.locations.teryt.simc;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "zmiany")
@XmlAccessorType(XmlAccessType.FIELD)
public class LocalityChangeList {

    @XmlElement(name = "zmiana")
    List<LocalityChangeDTO> localityChangeList;

    public List<LocalityChangeDTO> getLocalityChangeList() {
        return localityChangeList;
    }

}
