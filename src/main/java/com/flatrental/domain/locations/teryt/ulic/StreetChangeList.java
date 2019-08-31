package com.flatrental.domain.locations.teryt.ulic;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "zmiany")
@XmlAccessorType(XmlAccessType.FIELD)
public class StreetChangeList {

    @XmlElement(name = "zmiana")
    List<StreetChangeDTO> streetChangeList;

    public List<StreetChangeDTO> getStreetChangeList() {
        return streetChangeList;
    }
}
