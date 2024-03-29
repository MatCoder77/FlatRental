package com.flatrental.domain.locations.teryt.ulic;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@XmlRootElement(name = "catalog")
@XmlAccessorType(XmlAccessType.FIELD)
public class StreetList {

    @XmlElement(name = "row")
    private List<StreetDTO> streetList;


    public List<StreetDTO> getStreetList() {
        return Optional.ofNullable(streetList)
                .orElse(Collections.emptyList());
    }

}
