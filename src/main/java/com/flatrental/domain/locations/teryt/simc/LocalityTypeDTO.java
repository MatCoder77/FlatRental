package com.flatrental.domain.locations.teryt.simc;

import com.flatrental.domain.locations.teryt.terc.DateAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@XmlRootElement(name = "row")
@XmlAccessorType(XmlAccessType.FIELD)
public class LocalityTypeDTO {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @XmlElement(name = "RM")
    private String code;

    @XmlElement(name = "NAZWA_RM")
    private String name;

    @XmlElement(name = "STAN_NA")
    @XmlJavaTypeAdapter(DateAdapter.class)
    private Date lastUpdateDate;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof LocalityTypeDTO)) {
            return false;
        }

        LocalityTypeDTO otherType = (LocalityTypeDTO) obj;
        return otherType.code.equals(code) &&
                otherType.name.equals(name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name);
    }

    @Override
    public String toString() {
        return "LocalityTypeDTO{ RM = " + code
                + ", NAZWA_RM = " + name
                + ", STAN_NA = " + dateFormat.format(lastUpdateDate) + " }";
    }

}
