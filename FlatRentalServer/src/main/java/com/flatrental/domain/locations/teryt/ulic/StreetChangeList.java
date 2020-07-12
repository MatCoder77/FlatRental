package com.flatrental.domain.locations.teryt.ulic;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@XmlRootElement(name = "zmiany")
@XmlAccessorType(XmlAccessType.FIELD)
public class StreetChangeList {

    @XmlElement(name = "zmiana")
    List<StreetChangeDTO> changes;

    public List<StreetChangeDTO> getChanges() {
        return Optional.ofNullable(changes)
                .orElse(Collections.emptyList());
    }
}
