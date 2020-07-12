package com.flatrental.domain.locations.teryt.simc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@XmlRootElement(name = "catalog")
@XmlAccessorType(XmlAccessType.FIELD)
public class LocalityList {

    @XmlElement(name = "row")
    private List<LocalityDTO> localities;

    public List<LocalityDTO> getLocalities() {
        return Optional.ofNullable(localities)
                .orElse(Collections.emptyList());
    }

}
