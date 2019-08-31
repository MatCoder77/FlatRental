package com.flatrental.domain.locations.teryt.simc;

import com.flatrental.domain.locations.teryt.terc.AdministrationUnitTypeCode;
import com.flatrental.domain.locations.teryt.terc.AdministrationUnitTypeCodeAdapter;
import com.flatrental.domain.locations.teryt.terc.DateAdapter;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@XmlRootElement(name = "row")
@XmlAccessorType(XmlAccessType.FIELD)
@NoArgsConstructor
public class LocalityDTO implements Serializable {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @XmlElement(name = "WOJ")
    private String voivodeshipCode;

    @XmlElement(name = "POW")
    private String districtCode;

    @XmlElement(name = "GMI")
    private String communeCode;

    @XmlElement(name = "RODZ_GMI")
    @XmlJavaTypeAdapter(AdministrationUnitTypeCodeAdapter.class)
    private AdministrationUnitTypeCode unitTypeCode;

    @XmlElement(name = "RM")
    private String localityTypeCode;

    @XmlElement(name = "MZ")
    private Boolean hasCustomaryName;

    @XmlElement(name = "NAZWA")
    private String name;

    @XmlElement(name = "SYM")
    private String localityCode;

    @XmlElement(name = "SYMPOD")
    private String directParentCode;

    @XmlElement(name = "STAN_NA")
    @XmlJavaTypeAdapter(DateAdapter.class)
    private Date lastUpdateDate;


    public String getVoivodeshipCode() {
        return voivodeshipCode;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public String getCommuneCode() {
        return communeCode;
    }

    public AdministrationUnitTypeCode getUnitTypeCode() {
        return unitTypeCode;
    }

    public String getLocalityTypeCode() {
        return localityTypeCode;
    }

    public Boolean getHasCustomaryName() {
        return hasCustomaryName;
    }

    public String getName() {
        return name;
    }

    public String getLocalityCode() {
        return localityCode;
    }

    public String getDirectParentCode() {
        return directParentCode;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setVoivodeshipCode(String voivodeshipCode) {
        this.voivodeshipCode = voivodeshipCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public void setCommuneCode(String communeCode) {
        this.communeCode = communeCode;
    }

    public void setUnitTypeCode(AdministrationUnitTypeCode unitTypeCode) {
        this.unitTypeCode = unitTypeCode;
    }

    public void setLocalityTypeCode(String localityTypeCode) {
        this.localityTypeCode = localityTypeCode;
    }

    public void setHasCustomaryName(Boolean hasCustomaryName) {
        this.hasCustomaryName = hasCustomaryName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocalityCode(String localityCode) {
        this.localityCode = localityCode;
    }

    public void setDirectParentCode(String directParentCode) {
        this.directParentCode = directParentCode;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    private LocalityDTO(Builder builder) {
        this.voivodeshipCode = builder.voivodeshipCode;
        this.districtCode = builder.districtCode;
        this.communeCode = builder.communeCode;
        this.unitTypeCode = builder.unitTypeCode;
        this.localityTypeCode = builder.localityTypeCode;
        this.hasCustomaryName = builder.hasCustomaryName;
        this.name = builder.name;
        this.localityCode = builder.localityCode;
        this.directParentCode = builder.directParentCode;
        this.lastUpdateDate = builder.lastUpdateDate;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof LocalityDTO)) {
            return false;
        }
        LocalityDTO otherLocality = (LocalityDTO) obj;
        return otherLocality.localityCode.equals(localityCode) &&
                otherLocality.directParentCode.equals(directParentCode) &&
                otherLocality.unitTypeCode.equals(unitTypeCode) &&
                otherLocality.localityTypeCode.equals(localityTypeCode) &&
                otherLocality.name.equals(name) &&
                otherLocality.voivodeshipCode.equals(voivodeshipCode) &&
                otherLocality.districtCode.equals(districtCode) &&
                otherLocality.communeCode.equals(communeCode) &&
                otherLocality.hasCustomaryName.equals(hasCustomaryName);

    }

    @Override
    public int hashCode() {
        return Objects.hash(localityCode, directParentCode, unitTypeCode, localityTypeCode, name, voivodeshipCode, districtCode, communeCode, hasCustomaryName);
    }

    @Override
    public String toString() {
        return "LocalityDTO{ WOJ = " + voivodeshipCode
                + ", POW = " + districtCode
                + ", GMI = " + communeCode
                + ", RODZ_GMI = " + unitTypeCode.getCode()
                + ", RM = " + localityTypeCode
                + ", MZ = " + hasCustomaryName
                + ", NAZWA = " + name
                + ", SYM = " + localityCode
                + ", SYMPOD = " + directParentCode
                + ", STAN_NA = " + dateFormat.format(lastUpdateDate) + " }";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(LocalityDTO localityDTO) {
        return new Builder(localityDTO);
    }

    public static class Builder {

        private String voivodeshipCode;
        private String districtCode;
        private String communeCode;
        private AdministrationUnitTypeCode unitTypeCode;
        private String localityTypeCode;
        private Boolean hasCustomaryName;
        private String name;
        private String localityCode;
        private String directParentCode;
        private Date lastUpdateDate;

        private Builder() {}

        private Builder(LocalityDTO localityDTO) {
            this.voivodeshipCode = localityDTO.voivodeshipCode;
            this.districtCode = localityDTO.districtCode;
            this.communeCode = localityDTO.communeCode;
            this.unitTypeCode = localityDTO.unitTypeCode;
            this.localityTypeCode = localityDTO.localityTypeCode;
            this.hasCustomaryName = localityDTO.hasCustomaryName;
            this.name = localityDTO.name;
            this.localityCode = localityDTO.localityCode;
            this.directParentCode = localityDTO.directParentCode;
            this.lastUpdateDate = localityDTO.lastUpdateDate;
        }

        public Builder withVoivodeshipCode(String voivodeshipCode) {
            this.voivodeshipCode = voivodeshipCode;
            return this;
        }

        public Builder withDistrictCode(String districtCode) {
            this.districtCode = districtCode;
            return this;
        }

        public Builder withCommuneCode(String communeCode) {
            this.communeCode = communeCode;
            return this;
        }

        public Builder withUnitTypeCode(AdministrationUnitTypeCode unitTypeCode) {
            this.unitTypeCode = unitTypeCode;
            return this;
        }

        public Builder withLocalityTypeCode(String localityTypeCode) {
            this.localityTypeCode = localityTypeCode;
            return this;
        }

        public Builder withHasCustomaryName(Boolean hasCustomaryName) {
            this.hasCustomaryName = hasCustomaryName;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withLocalityCode(String localityCode) {
            this.localityCode = localityCode;
            return this;
        }

        public Builder withDirectParentCode(String directParentCode) {
            this.directParentCode = directParentCode;
            return this;
        }

        public Builder withLastUpdateDate(Date lastUpdateDate) {
            this.lastUpdateDate = lastUpdateDate;
            return this;
        }

        public LocalityDTO build() {
            return new LocalityDTO(this);
        }
    }

}
