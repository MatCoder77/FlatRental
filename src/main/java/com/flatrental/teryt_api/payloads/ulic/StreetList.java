package com.flatrental.teryt_api.payloads.ulic;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "catalog")
@XmlAccessorType(XmlAccessType.FIELD)
public class StreetList {

    @XmlElement(name = "row")
    private List<StreetDTO> streetList;


    public List<StreetDTO> getStreetList() {
        return streetList;
    }

}
