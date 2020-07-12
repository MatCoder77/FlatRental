package com.flatrental.domain.locations.teryt.terc;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.Date;
import java.util.Optional;

@XmlRootElement(name = "zmiana")
@XmlAccessorType(XmlAccessType.FIELD)
public class AdministrationUnitChangeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = "TypKorekty")
    @XmlJavaTypeAdapter(AdministrationUnitCorrectionTypeAdapter.class)
    private AdministrationUnitCorrectionType correctionType;

    @XmlElement(name = "WojPrzed")
    @XmlJavaTypeAdapter(SetNullIfBlankStringAdapter.class)
    private String voivodeshipCodeBefore;

    @XmlElement(name = "PowPrzed")
    @XmlJavaTypeAdapter(SetNullIfBlankStringAdapter.class)
    private String districtCodeBefore;

    @XmlElement(name = "GmiPrzed")
    @XmlJavaTypeAdapter(SetNullIfBlankStringAdapter.class)
    private String communeCodeBefore;

    @XmlElement(name = "RodzPrzed")
    @XmlJavaTypeAdapter(AdministrationUnitTypeCodeAdapter.class)
    private AdministrationUnitTypeCode unitTypeCodeBefore;

    @XmlElement(name = "NazwaPrzed")
    @XmlJavaTypeAdapter(SetNullIfBlankStringAdapter.class)
    private String nameBefore;

    @XmlElement(name = "NazwaDodatkowaPrzed")
    @XmlJavaTypeAdapter(AdministrationUnitTypeAdapter.class)
    private AdministrationUnitTypeDTO unitTypeBefore;

    @XmlElement(name = "StanPrzed")
    @XmlJavaTypeAdapter(DateAdapter.class)
    private Date lastUpdateDateBefore;

    @XmlElement(name = "WojPo")
    @XmlJavaTypeAdapter(SetNullIfBlankStringAdapter.class)
    private String voivodeshipCodeAfter;

    @XmlElement(name = "PowPo")
    @XmlJavaTypeAdapter(SetNullIfBlankStringAdapter.class)
    private String districtCodeAfter;

    @XmlElement(name = "GmiPo")
    @XmlJavaTypeAdapter(SetNullIfBlankStringAdapter.class)
    private String communeCodeAfter;

    @XmlElement(name = "RodzPo")
    @XmlJavaTypeAdapter(AdministrationUnitTypeCodeAdapter.class)
    private AdministrationUnitTypeCode unitTypeCodeAfter;

    @XmlElement(name = "NazwaPo")
    @XmlJavaTypeAdapter(SetNullIfBlankStringAdapter.class)
    private String nameAfter;

    @XmlElement(name = "NazwaDodatkowaPo")
    @XmlJavaTypeAdapter(AdministrationUnitTypeAdapter.class)
    private AdministrationUnitTypeDTO unitTypeAfter;

    @XmlElement(name = "WyodrebnionoZIdentyfikatora1")
    @XmlJavaTypeAdapter(SetNullIfBlankStringAdapter.class)
    private String excludedFromUnitIdentifier1;

    @XmlElement(name = "WyodrebnionoZIdentyfikatora2")
    @XmlJavaTypeAdapter(SetNullIfBlankStringAdapter.class)
    private String excludedFromUnitIdentifier2;

    @XmlElement(name = "WyodrebnionoZIdentyfikatora3")
    @XmlJavaTypeAdapter(SetNullIfBlankStringAdapter.class)
    private String excludedFromUnitIdentifier3;

    @XmlElement(name = "WlaczonoDoIdentyfikatora1")
    @XmlJavaTypeAdapter(SetNullIfBlankStringAdapter.class)
    private String includedToUnitIdentifier1;

    @XmlElement(name = "WlaczonoDoIdentyfikatora2")
    @XmlJavaTypeAdapter(SetNullIfBlankStringAdapter.class)
    private String includedToUnitIdentifier2;

    @XmlElement(name = "WlaczonoDoIdentyfikatora3")
    @XmlJavaTypeAdapter(SetNullIfBlankStringAdapter.class)
    private String includedToUnitIdentifier3;

    @XmlElement(name = "StanPo")
    @XmlJavaTypeAdapter(DateAdapter.class)
    private Date lastUpdateDateAfter;


    public AdministrationUnitCorrectionType getCorrectionType() {
        return correctionType;
    }

    public Date getLastUpdateDateBefore() {
        return lastUpdateDateBefore;
    }

    public String getVoivodeshipCodeBefore() {
        return voivodeshipCodeBefore;
    }

    public String getDistrictCodeBefore() {
        return districtCodeBefore;
    }

    public String getCommuneCodeBefore() {
        return communeCodeBefore;
    }

    public AdministrationUnitTypeCode getUnitTypeCodeBefore() {
        return unitTypeCodeBefore;
    }

    public String getNameBefore() {
        return nameBefore;
    }

    public AdministrationUnitTypeDTO getUnitTypeBefore() {
        return unitTypeBefore;
    }

    public Date getLastUpdateDateAfter() {
        return lastUpdateDateAfter;
    }

    public String getVoivodeshipCodeAfter() {
        return voivodeshipCodeAfter;
    }

    public String getDistrictCodeAfter() {
        return districtCodeAfter;
    }

    public String getCommuneCodeAfter() {
        return communeCodeAfter;
    }

    public AdministrationUnitTypeCode getUnitTypeCodeAfter() {
        return unitTypeCodeAfter;
    }

    public String getNameAfter() {
        return nameAfter;
    }

    public AdministrationUnitTypeDTO getUnitTypeAfter() {
        return unitTypeAfter;
    }

    public String getIncludedToUnitIdentifier1() {
        return includedToUnitIdentifier1;
    }

    public String getIncludedToUnitIdentifier2() {
        return includedToUnitIdentifier2;
    }

    public String getIncludedToUnitIdentifier3() {
        return includedToUnitIdentifier3;
    }

    public String getExcludedFromUnitIdentifier1() {
        return excludedFromUnitIdentifier1;
    }

    public String getExcludedFromUnitIdentifier2() {
        return excludedFromUnitIdentifier2;
    }

    public String getExcludedFromUnitIdentifier3() {
        return excludedFromUnitIdentifier3;
    }

    public AdministrationUnitDTO getAdministrationUnitBeforeChange() {
        return AdministrationUnitDTO.builder()
                .withVoivodeshipCode(voivodeshipCodeBefore)
                .withDistrictCode(districtCodeBefore)
                .withCommuneCode(communeCodeBefore)
                .withUnitTypeCode(unitTypeCodeBefore)
                .withName(nameBefore)
                .withUnitType(unitTypeBefore)
                .withLastUpdateDate(lastUpdateDateBefore)
                .build();
    }

    public AdministrationUnitDTO getAdministrationUnitAfterChange() {
        AdministrationUnitDTO.Builder builder = AdministrationUnitDTO.builder(getAdministrationUnitBeforeChange());
        Optional.ofNullable(voivodeshipCodeAfter).ifPresent(builder::withVoivodeshipCode);
        Optional.ofNullable(districtCodeAfter).ifPresent(builder::withDistrictCode);
        Optional.ofNullable(communeCodeAfter).ifPresent(builder::withCommuneCode);
        Optional.ofNullable(unitTypeCodeAfter).ifPresent(builder::withUnitTypeCode);
        Optional.ofNullable(nameAfter).ifPresent(builder::withName);
        Optional.ofNullable(unitTypeAfter).ifPresent(builder::withUnitType);
        Optional.ofNullable(lastUpdateDateAfter).ifPresent(builder::withLastUpdateDate);
        return builder.build();
    }

}