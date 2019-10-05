package com.flatrental.domain.locations.localitypart;


import com.flatrental.domain.locations.abstractlocality.AbstractLocality;
import com.flatrental.domain.locations.abstractlocality.GenericLocalityType;
import com.flatrental.domain.locations.locality.Locality;
import com.flatrental.domain.locations.localitydistrict.LocalityDistrict;
import com.flatrental.domain.locations.localitytype.LocalityType;
import com.flatrental.domain.locations.street.Street;
import lombok.NoArgsConstructor;

import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.text.MessageFormat;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@NoArgsConstructor
public class LocalityPart {

    private Long id;

    @NotNull
    private LocalityType localityType;

    @NotNull
    private Boolean hasCustomaryName;

    @NotNull
    private String name;

    @NotNull
    @Size(min = 7, max = 7)
    private String code;

    @NotNull
    private Locality parentLocality;

    private LocalityDistrict localityDistrict;

    private Set<Street> streets;

    public static LocalityPart fromAbstractLocality(AbstractLocality abstractLocality) {
        validateGenericType(abstractLocality);
        return new LocalityPart(
                abstractLocality.getId(),
                abstractLocality.getLocalityType(),
                abstractLocality.getHasCustomaryName(),
                abstractLocality.getName(),
                abstractLocality.getCode(),
                Locality.fromAbstractLocality(abstractLocality.getParentLocality()),
                Optional.ofNullable(abstractLocality.getLocalityDistrict()).map(LocalityDistrict::fromAbstractLocality).orElse(null),
                abstractLocality.getStreets());
    }

    private static void validateGenericType(AbstractLocality abstractLocality) {
        if (abstractLocality.getGenericLocalityType() != GenericLocalityType.LOCALITY_PART) {
            throw new IllegalArgumentException(MessageFormat.format("Cannot convert from AbstractLocality with generic type {0} to LocalityPart", abstractLocality.getGenericLocalityType()));
        }
    }

    public LocalityPart(Long id, LocalityType localityType, Boolean hasCustomaryName, String name, String code, Locality parentLocality, LocalityDistrict localityDistrict, Set<Street> streets) {
        this.id = id;
        this.localityType = localityType;
        this.hasCustomaryName = hasCustomaryName;
        this.name = name;
        this.code = code;
        this.parentLocality = parentLocality;
        this.localityDistrict = localityDistrict;
        this.streets = streets;
    }

    public LocalityPart(LocalityType localityType, Boolean hasCustomaryName, String name, String code, Locality parentLocality, LocalityDistrict localityDistrict) {
        this(null, localityType, hasCustomaryName, name, code, parentLocality, localityDistrict, null);
    }

    public Long getId() {
        return id;
    }

    public LocalityType getLocalityType() {
        return localityType;
    }

    public void setLocalityType(LocalityType localityType) {
        this.localityType = localityType;
    }

    public Boolean getHasCustomaryName() {
        return hasCustomaryName;
    }

    public void setHasCustomaryName(Boolean hasCustomaryName) {
        this.hasCustomaryName = hasCustomaryName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Locality getParentLocality() {
        return parentLocality;
    }

    public void setParentLocality(Locality parentLocality) {
        this.parentLocality = parentLocality;
    }

    public Optional<LocalityDistrict> getLocalityDistrict() {
        return Optional.ofNullable(localityDistrict);
    }

    public void setLocalityDistrict(LocalityDistrict localityDistrict) {
        this.localityDistrict = localityDistrict;
    }

    public Set<Street> getStreets() {
        return streets;
    }

    public void setStreets(Set<Street> streets) {
        this.streets = streets;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof LocalityPart)) {
            return false;
        }

        LocalityPart otherLocalityPart = (LocalityPart) obj;
        return Objects.equals(otherLocalityPart.id, id) &&
                otherLocalityPart.code.equals(code) &&
                otherLocalityPart.name.equals(name) &&
                otherLocalityPart.hasCustomaryName.equals(hasCustomaryName) &&
                otherLocalityPart.localityType.equals(localityType) &&
                otherLocalityPart.parentLocality.equals(parentLocality) &&
                Objects.equals(otherLocalityPart.localityDistrict, localityDistrict);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, name, hasCustomaryName, localityType, parentLocality, localityDistrict);
    }

    @Override
    public String toString() {
        return "LocalityPart{ id = " + id.toString()
                + ", locality type = " + localityType.getTypeName()
                + ", customary name = " + hasCustomaryName
                + ", name = " + name
                + ", code = " + code
                + ", parent locality code = " + parentLocality.getCode()
                + ", locality district code = " + getLocalityDistrict().map(LocalityDistrict::getCode).orElse("null")
                + " }";
    }

}
