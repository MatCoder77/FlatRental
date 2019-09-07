package com.flatrental.domain.locations.teryt.ulic;

import com.flatrental.domain.locations.teryt.terc.AdministrationUnitTypeCode;
import com.flatrental.domain.locations.teryt.terc.AdministrationUnitTypeCodeAdapter;
import com.flatrental.domain.locations.teryt.terc.DateAdapter;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@XmlRootElement(name = "row")
@XmlAccessorType(XmlAccessType.FIELD)
@NoArgsConstructor
public class StreetDTO {

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

    @XmlElement(name = "SYM")
    private String directParentCode;

    @XmlElement(name = "SYM_UL")
    private String streetCode;

    @XmlElement(name = "CECHA")
    @XmlJavaTypeAdapter(StreetTypeAdapter.class)
    private StreetType streetType;

    @XmlElement(name = "NAZWA_1")
    private String mainName;

    @XmlElement(name = "NAZWA_2")
    @XmlJavaTypeAdapter(StreetNameAdapter.class)
    private Optional<String> leadingName;

    @XmlElement(name = "STAN_NA")
    @XmlJavaTypeAdapter(DateAdapter.class)
    private Date lastUpdateDate;


    StreetDTO(Builder builder) {
        this.voivodeshipCode = builder.voivodeshipCode;
        this.districtCode = builder.districtCode;
        this.communeCode = builder.communeCode;
        this.unitTypeCode = builder.unitTypeCode;
        this.directParentCode = builder.directParentCode;
        this.streetCode = builder.streetCode;
        this.streetType = builder.streetType;
        this.mainName = builder.mainName;
        this.leadingName = builder.leadingName;
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

    public String getDirectParentCode() {
        return directParentCode;
    }

    public String getStreetCode() {
        return streetCode;
    }

    public StreetType getStreetType() {
        return streetType;
    }

    public String getMainName() {
        return mainName;
    }

    public Optional<String> getLeadingName() {
        return leadingName;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof StreetDTO)) {
            return false;
        }

        StreetDTO otherStreet = (StreetDTO) obj;
        return otherStreet.streetCode.equals(streetCode) &&
                otherStreet.directParentCode.equals(directParentCode) &&
                otherStreet.streetType.equals(streetType) &&
                otherStreet.mainName.equals(mainName) &&
                otherStreet.leadingName.equals(leadingName) &&
                otherStreet.voivodeshipCode.equals(voivodeshipCode) &&
                otherStreet.districtCode.equals(districtCode) &&
                otherStreet.communeCode.equals(communeCode) &&
                otherStreet.unitTypeCode.equals(unitTypeCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(streetCode, directParentCode, streetType, mainName, leadingName, voivodeshipCode, districtCode, communeCode, unitTypeCode);
    }

    @Override
    public String toString() {
        return "StreetDTO{ WOJ = " + voivodeshipCode
                + ", POW = " + districtCode
                + ", GMI = " + communeCode
                + ", RODZ_GMI = " + unitTypeCode.getCode()
                + ", SYM = " + directParentCode
                + ", SYM_UL = " + streetCode
                + ", CECHA = " + streetType
                + ", NAZWA_1 = " + mainName
                + ", NAZWA_2 = " + leadingName.orElse("")
                + ", STAN_NA = " + dateFormat.format(lastUpdateDate) + " }";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(StreetDTO streetDTO) {
        return new Builder(streetDTO);
    }

    public static class Builder {

        private String voivodeshipCode;
        private String districtCode;
        private String communeCode;
        private AdministrationUnitTypeCode unitTypeCode;
        private String directParentCode;
        private String streetCode;
        private StreetType streetType;
        private String mainName;
        private Optional<String> leadingName;
        private Date lastUpdateDate;

        private Builder(){}

        private Builder(StreetDTO streetDTO) {
            this.voivodeshipCode = streetDTO.voivodeshipCode;
            this.districtCode = streetDTO.districtCode;
            this.communeCode = streetDTO.communeCode;
            this.unitTypeCode = streetDTO.unitTypeCode;
            this.directParentCode = streetDTO.directParentCode;
            this.streetCode = streetDTO.streetCode;
            this.streetType = streetDTO.streetType;
            this.mainName = streetDTO.mainName;
            this.leadingName = streetDTO.leadingName;
            this.lastUpdateDate = streetDTO.lastUpdateDate;
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

        public Builder withDirectParentCode(String directParentCode) {
            this.directParentCode = directParentCode;
            return this;
        }

        public Builder withStreetCode(String streetCode) {
            this.streetCode = streetCode;
            return this;
        }

        public Builder withStreetType(StreetType streetType) {
            this.streetType = streetType;
            return this;
        }

        public Builder withMainName(String mainName) {
            this.mainName = mainName;
            return this;
        }

        public Builder withLeadingName(Optional<String> leadingName) {
            this.leadingName = leadingName;
            return this;
        }

        public Builder withLastUpdateDate(Date lastUpdateDate) {
            this.lastUpdateDate = lastUpdateDate;
            return this;
        }

        public StreetDTO build() {
            return new StreetDTO(this);
        }

    }

}
