package com.flatrental.domain.locations.teryt.ulic;

import com.flatrental.domain.locations.teryt.terc.AdministrationUnitTypeCode;
import com.flatrental.domain.locations.teryt.terc.AdministrationUnitTypeCodeAdapter;
import com.flatrental.domain.locations.teryt.terc.DateAdapter;
import com.flatrental.domain.locations.teryt.terc.SetNullIfBlankStringAdapter;

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
public class StreetChangeDTO implements Serializable {

    @XmlElement(name = "TypKorekty")
    @XmlJavaTypeAdapter(StreetCorrectionTypeAdapter.class)
    private StreetCorrectionType correctionType;

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

    @XmlElement(name = "IdentyfikatorMiejscowosciPrzed")
    @XmlJavaTypeAdapter(SetNullIfBlankStringAdapter.class)
    private String directParentCodeBefore;

    @XmlElement(name = "NazwaMiejscowosciPrzed")
    @XmlJavaTypeAdapter(SetNullIfBlankStringAdapter.class)
    private String localityNameBefore;

    @XmlElement(name = "IdentyfikatorMiejscowosciPodstawowejPrzed")
    @XmlJavaTypeAdapter(SetNullIfBlankStringAdapter.class)
    private String parentCodeBefore;

    @XmlElement(name = "IdentyfikatorNazwyUlicyPrzed")
    @XmlJavaTypeAdapter(SetNullIfBlankStringAdapter.class)
    private String codeBefore;

    @XmlElement(name = "CechaPrzed")
    @XmlJavaTypeAdapter(StreetTypeAdapter.class)
    private StreetType streetTypeBefore;

    @XmlElement(name = "NazwaUlicyWPelnymBrzmienuPrzed")
    @XmlJavaTypeAdapter(SetNullIfBlankStringAdapter.class)
    private String fullNameBefore;

    @XmlElement(name = "Nazwa1Przed")
    @XmlJavaTypeAdapter(SetNullIfBlankStringAdapter.class)
    private String mainNameBefore;

    @XmlElement(name = "Nazwa2Przed")
    @XmlJavaTypeAdapter(SetNullIfBlankStringAdapter.class)
    private String leadingNameBefore;

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

    @XmlElement(name = "IdentyfikatorMiejscowosciPo")
    @XmlJavaTypeAdapter(SetNullIfBlankStringAdapter.class)
    private String directParentCodeAfter;

    @XmlElement(name = "NazwaMiejscowosciPo")
    @XmlJavaTypeAdapter(SetNullIfBlankStringAdapter.class)
    private String localityNameAfter;

    @XmlElement(name = "IdentyfikatorMiejscowosciPodstawowejPo")
    @XmlJavaTypeAdapter(SetNullIfBlankStringAdapter.class)
    private String parentCodeAfter;

    @XmlElement(name = "IdentyfikatorNazwyUlicyPo")
    @XmlJavaTypeAdapter(SetNullIfBlankStringAdapter.class)
    private String codeAfter;

    @XmlElement(name = "CechaPo")
    @XmlJavaTypeAdapter(StreetTypeAdapter.class)
    private StreetType streetTypeAfter;

    @XmlElement(name = "NazwaUlicyWPelnymBrzmienuPo")
    @XmlJavaTypeAdapter(SetNullIfBlankStringAdapter.class)
    private String fullNameAfter;

    @XmlElement(name = "Nazwa1Po")
    @XmlJavaTypeAdapter(SetNullIfBlankStringAdapter.class)
    private String mainNameAfter;

    @XmlElement(name = "Nazwa2Po")
    @XmlJavaTypeAdapter(SetNullIfBlankStringAdapter.class)
    private String leadingNameAfter;

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

    public StreetCorrectionType getCorrectionType() {
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

    public String getDirectParentCodeBefore() {
        return directParentCodeBefore;
    }

    public String getParentCodeBefore() {
        return parentCodeBefore;
    }

    public String getLocalityNameBefore() {
        return localityNameBefore;
    }

    public String getCodeBefore() {
        return codeBefore;
    }

    public StreetType getStreetTypeBefore() {
        return streetTypeBefore;
    }

    public String getMainNameBefore() {
        return mainNameBefore;
    }

    public String getLeadingNameBefore() {
        return leadingNameBefore;
    }

    public String getFullNameBefore() {
        return fullNameBefore;
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

    public String getDirectParentCodeAfter() {
        return directParentCodeAfter;
    }

    public String getParentCodeAfter() {
        return parentCodeAfter;
    }

    public String getLocalityNameAfter() {
        return localityNameAfter;
    }

    public String getCodeAfter() {
        return codeAfter;
    }

    public StreetType getStreetTypeAfter() {
        return streetTypeAfter;
    }

    public String getMainNameAfter() {
        return mainNameAfter;
    }

    public String getLeadingNameAfter() {
        return leadingNameAfter;
    }

    public String getFullNameAfter() {
        return fullNameAfter;
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

    public String getIncludedToUnitIdentifier1() {
        return includedToUnitIdentifier1;
    }

    public String getIncludedToUnitIdentifier2() {
        return includedToUnitIdentifier2;
    }

    public String getIncludedToUnitIdentifier3() {
        return includedToUnitIdentifier3;
    }

    public StreetDTO getStreetDTOBeforeChange() {
        return StreetDTO.builder()
                .withVoivodeshipCode(voivodeshipCodeBefore)
                .withDistrictCode(districtCodeBefore)
                .withCommuneCode(communeCodeBefore)
                .withUnitTypeCode(unitTypeCodeBefore)
                .withDirectParentCode(directParentCodeBefore)
                .withStreetCode(codeBefore)
                .withStreetType(streetTypeBefore)
                .withMainName(mainNameBefore)
                .withLeadingName(Optional.ofNullable(leadingNameBefore))
                .withLastUpdateDate(lastUpdateDateBefore)
                .build();
    }

    public StreetDTO getStreetDTOAfterChange() {
        StreetDTO.Builder builder = StreetDTO.builder(getStreetDTOBeforeChange());
        Optional.ofNullable(voivodeshipCodeAfter).ifPresent(builder::withVoivodeshipCode);
        Optional.ofNullable(districtCodeAfter).ifPresent(builder::withDistrictCode);
        Optional.ofNullable(communeCodeAfter).ifPresent(builder::withCommuneCode);
        Optional.ofNullable(unitTypeCodeAfter).ifPresent(builder::withUnitTypeCode);
        Optional.ofNullable(directParentCodeAfter).ifPresent(builder::withDirectParentCode);
        Optional.ofNullable(codeAfter).ifPresent(builder::withStreetCode);
        Optional.ofNullable(streetTypeAfter).ifPresent(builder::withStreetType);
        Optional.ofNullable(mainNameAfter).ifPresent(builder::withMainName);
        Optional.ofNullable(leadingNameAfter).ifPresent(name -> builder.withLeadingName(Optional.ofNullable(name)));
        Optional.ofNullable(lastUpdateDateAfter).ifPresent(builder::withLastUpdateDate);
        return builder.build();
    }

}
