package com.flatrental.domain.locations.teryt.simc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "catalog")
@XmlAccessorType(XmlAccessType.FIELD)
public class LocalityTypeList {

    @XmlElement(name = "row")
    private List<LocalityTypeDTO> localityTypeList;

    public List<LocalityTypeDTO> getLocalityTypeList() {
        return localityTypeList;
    }
}
