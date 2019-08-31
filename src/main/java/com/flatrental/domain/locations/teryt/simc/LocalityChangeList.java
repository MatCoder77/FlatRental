package com.flatrental.domain.locations.teryt.simc;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@XmlRootElement(name = "zmiany")
@XmlAccessorType(XmlAccessType.FIELD)
public class LocalityChangeList {

    @XmlElement(name = "zmiana")
    List<LocalityChangeDTO> localityChangeList;

    public List<LocalityChangeDTO> getLocalityChangeList() {
        return Optional.ofNullable(localityChangeList)
                .orElse(Collections.emptyList());
    }

}
