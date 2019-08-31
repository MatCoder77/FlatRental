package com.flatrental.teryt_api.payloads.simc;

import com.flatrental.teryt_api.payloads.terc.AdministrationUnitTypeCode;
import com.flatrental.teryt_api.payloads.terc.AdministrationUnitTypeCodeAdapter;
import com.flatrental.teryt_api.payloads.terc.DateAdapter;
import com.flatrental.teryt_api.payloads.terc.SetNullIfBlankStringAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@XmlRootElement(name = "zmiana")
@XmlAccessorType(XmlAccessType.FIELD)
public class LocalityChangeDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @XmlElement(name = "TypKorekty")
    @XmlJavaTypeAdapter(LocalityCorrectionTypeAdapter.class)
    private LocalityCorrectionType correctionType;

    @XmlElement(name = "Identyfikator")
    @XmlJavaTypeAdapter(SetNullIfBlankStringAdapter.class)
    private String localityCode;

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

    @XmlElement(name = "RodzajMiejscowosciPrzed")
    @XmlJavaTypeAdapter(SetNullIfBlankStringAdapter.class)
    private String localityTypeCodeBefore;

    @XmlElement(name = "CzyNazwaZwyczajowaPrzed")
    private Boolean hasCustomaryNameBefore;

    @XmlElement(name = "IdentyfikatorMiejscowosciPodstawowejPrzed")
    @XmlJavaTypeAdapter(SetNullIfBlankStringAdapter.class)
    private String directParentCodeBefore;

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

    @XmlElement(name = "RodzajMiejscowosciPo")
    @XmlJavaTypeAdapter(SetNullIfBlankStringAdapter.class)
    private String localityTypeCodeAfter;

    @XmlElement(name = "CzyNazwaZwyczajowaPo")
    private Boolean hasCustomaryNameAfter;

    @XmlElement(name = "IdentyfikatorMiejscowosciPodstawowejPo")
    @XmlJavaTypeAdapter(SetNullIfBlankStringAdapter.class)
    private String directParentCodeAfter;

    @XmlElement(name = "WyodrebnionoZIdentyfikatora1")
    @XmlJavaTypeAdapter(SetNullIfBlankStringAdapter.class)
    private String excludedFromUnitIdentifier1;

    @XmlElement(name = "WyodrebnionoZIdentyfikatora2")
    @XmlJavaTypeAdapter(SetNullIfBlankStringAdapter.class)
    private String excludedFromUnitIdentifier2;

    @XmlElement(name = "WyodrebnionoZIdentyfikatora3")
    @XmlJavaTypeAdapter(SetNullIfBlankStringAdapter.class)
    private String excludedFromUnitIdentifier3;

    @XmlElement(name = "WyodrebnionoZIdentyfikatora4")
    @XmlJavaTypeAdapter(SetNullIfBlankStringAdapter.class)
    private String excludedFromUnitIdentifier4;

    @XmlElement(name = "WlaczonoDoIdentyfikatora1")
    @XmlJavaTypeAdapter(SetNullIfBlankStringAdapter.class)
    private String includedToUnitIdentifier1;

    @XmlElement(name = "WlaczonoDoIdentyfikatora2")
    @XmlJavaTypeAdapter(SetNullIfBlankStringAdapter.class)
    private String includedToUnitIdentifier2;

    @XmlElement(name = "WlaczonoDoIdentyfikatora3")
    @XmlJavaTypeAdapter(SetNullIfBlankStringAdapter.class)
    private String includedToUnitIdentifier3;

    @XmlElement(name = "WlaczonoDoIdentyfikatora4")
    @XmlJavaTypeAdapter(SetNullIfBlankStringAdapter.class)
    private String includedToUnitIdentifier4;

    @XmlElement(name = "StanPo")
    @XmlJavaTypeAdapter(DateAdapter.class)
    private Date lastUpdateDateAfter;

    public LocalityCorrectionType getCorrectionType() {
        return correctionType;
    }

    public String getLocalityCode() {
        return localityCode;
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

    public String getLocalityTypeCodeBefore() {
        return localityTypeCodeBefore;
    }

    public Boolean getHasCustomaryNameBefore() {
        return hasCustomaryNameBefore;
    }

    public String getDirectParentCodeBefore() {
        return directParentCodeBefore;
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

    public String getLocalityTypeCodeAfter() {
        return localityTypeCodeAfter;
    }

    public Boolean getHasCustomaryNameAfter() {
        return hasCustomaryNameAfter;
    }

    public String getDirectParentCodeAfter() {
        return directParentCodeAfter;
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

    public String getExcludedFromUnitIdentifier4() {
        return excludedFromUnitIdentifier4;
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

    public String getIncludedToUnitIdentifier4() {
        return includedToUnitIdentifier4;
    }

    public LocalityDTO getLocalityDTOBeforeChange() {
        return LocalityDTO.builder()
                .withVoivodeshipCode(voivodeshipCodeBefore)
                .withDistrictCode(districtCodeBefore)
                .withCommuneCode(communeCodeBefore)
                .withUnitTypeCode(unitTypeCodeBefore)
                .withLocalityTypeCode(localityTypeCodeBefore)
                .withHasCustomaryName(hasCustomaryNameBefore)
                .withName(nameBefore)
                .withLocalityCode(localityCode)
                .withDirectParentCode(directParentCodeBefore)
                .withLastUpdateDate(lastUpdateDateBefore)
                .build();
    }

    public LocalityDTO getLocalityDTOAfterChange() {
        LocalityDTO.Builder builder = LocalityDTO.builder(getLocalityDTOBeforeChange());
        Optional.ofNullable(voivodeshipCodeAfter).ifPresent(builder::withVoivodeshipCode);
        Optional.ofNullable(districtCodeAfter).ifPresent(builder::withDistrictCode);
        Optional.ofNullable(communeCodeAfter).ifPresent(builder::withCommuneCode);
        Optional.ofNullable(unitTypeCodeAfter).ifPresent(builder::withUnitTypeCode);
        Optional.ofNullable(localityTypeCodeAfter).ifPresent(builder::withLocalityTypeCode);
        Optional.ofNullable(hasCustomaryNameAfter).ifPresent(builder::withHasCustomaryName);
        Optional.ofNullable(nameAfter).ifPresent(builder::withName);
        Optional.ofNullable(localityCode).ifPresent(builder::withLocalityCode);
        Optional.ofNullable(directParentCodeAfter).ifPresent(builder::withDirectParentCode);
        Optional.ofNullable(lastUpdateDateAfter).ifPresent(builder::withLastUpdateDate);
        return builder.build();
    }

}
