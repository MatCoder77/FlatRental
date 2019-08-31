package com.flatrental.teryt_api.payloads.terc;

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
public class AdministrationUnitDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @XmlElement(name = "WOJ")
    private String voivodeshipCode;

    @XmlElement(name = "POW")
    private String districtCode;

    @XmlElement(name = "GMI")
    private String communeCode;

    @XmlElement(name = "RODZ")
    @XmlJavaTypeAdapter(AdministrationUnitTypeCodeAdapter.class)
    private AdministrationUnitTypeCode unitTypeCode;

    @XmlElement(name = "NAZWA")
    private String name;

    @XmlElement(name = "NAZWA_DOD")
    @XmlJavaTypeAdapter(AdministrationUnitTypeAdapter.class)
    private AdministrationUnitTypeDTO unitType;

    @XmlElement(name = "STAN_NA")
    @XmlJavaTypeAdapter(DateAdapter.class)
    private Date lastUpdateDate;

    public AdministrationUnitDTO(Builder builder) {
        this.voivodeshipCode = builder.voivodeshipCode;
        this.districtCode = builder.districtCode;
        this.communeCode = builder.communeCode;
        this.unitTypeCode = builder.unitTypeCode;
        this.name = builder.name;
        this.unitType = builder.unitType;
        this.lastUpdateDate = builder.lastUpdateDate;
    }

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

    public String getName() {
        return name;
    }

    public AdministrationUnitTypeDTO getUnitType() {
        return unitType;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this)
            return true;
        if(!(obj instanceof AdministrationUnitDTO))
            return false;
        AdministrationUnitDTO otherUnit = (AdministrationUnitDTO) obj;
        return  otherUnit.name.equals(name) &&
                Objects.equals(otherUnit.unitTypeCode, unitTypeCode) &&
                otherUnit.communeCode.equals(communeCode) &&
                otherUnit.districtCode.equals(districtCode) &&
                otherUnit.voivodeshipCode.equals(voivodeshipCode) &&
                otherUnit.unitType.equals(unitType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, unitTypeCode, communeCode, districtCode, voivodeshipCode, unitType);
    }

    @Override
    public String toString() {
        return "AdministrationUnitDTO{ WOJ = " + voivodeshipCode
                + ", POW = " + districtCode
                + ", GMI =" + communeCode
                + ", RODZ = " + unitTypeCode.getCode()
                + ", NAZWA = " + name
                + ", NAZWA_DOD = " + unitType.getTypeValue()
                + ", STAN_NA = " + dateFormat.format(lastUpdateDate) + " }";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(AdministrationUnitDTO administrationUnitDTO) {
        return new Builder(administrationUnitDTO);
    }

    public static class Builder {

        private String voivodeshipCode;
        private String districtCode;
        private String communeCode;
        private AdministrationUnitTypeCode unitTypeCode;
        private String name;
        private AdministrationUnitTypeDTO unitType;
        private Date lastUpdateDate;

        private Builder(){}

        private Builder(AdministrationUnitDTO administrationUnitDTO) {
            this.voivodeshipCode = administrationUnitDTO.voivodeshipCode;
            this.districtCode = administrationUnitDTO.districtCode;
            this.communeCode = administrationUnitDTO.communeCode;
            this.unitTypeCode = administrationUnitDTO.unitTypeCode;
            this.name = administrationUnitDTO.name;
            this.unitType = administrationUnitDTO.unitType;
            this.lastUpdateDate = administrationUnitDTO.lastUpdateDate;
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

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withUnitType(AdministrationUnitTypeDTO unitType) {
            this.unitType = unitType;
            return this;
        }

        public Builder withLastUpdateDate(Date lastUpdateDate) {
            this.lastUpdateDate = lastUpdateDate;
            return this;
        }

        public AdministrationUnitDTO build() {
            return new AdministrationUnitDTO(this);
        }

    }

}
