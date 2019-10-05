package com.flatrental.domain.locations.localitydistrict;



import com.flatrental.domain.locations.abstractlocality.AbstractLocality;
import com.flatrental.domain.locations.abstractlocality.GenericLocalityType;
import com.flatrental.domain.locations.locality.Locality;
import com.flatrental.domain.locations.localitytype.LocalityType;
import com.flatrental.domain.locations.street.Street;

import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.text.MessageFormat;
import java.util.Objects;
import java.util.Set;

public class LocalityDistrict {

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

    private Set<Street> streets;


    public static LocalityDistrict fromAbstractLocality(AbstractLocality abstractLocality) {
        validateGenericType(abstractLocality);
        return new LocalityDistrict(
                abstractLocality.getId(),
                abstractLocality.getLocalityType(),
                abstractLocality.getHasCustomaryName(),
                abstractLocality.getName(),
                abstractLocality.getCode(),
                Locality.fromAbstractLocality(abstractLocality.getParentLocality()),
                abstractLocality.getStreets());
    }

    private static void validateGenericType(AbstractLocality abstractLocality) {
        if (abstractLocality.getGenericLocalityType() != GenericLocalityType.LOCALITY_DISTRICT) {
            throw new IllegalArgumentException(MessageFormat.format("Cannot convert AbstractLocality with generic type {0} to LocalityDistrict", abstractLocality.getGenericLocalityType()));
        }
    }

    public LocalityDistrict(Long id, LocalityType localityType, Boolean hasCustomaryName, String name, String code, Locality parentLocality, Set<Street> streets) {
        this.id = id;
        this.localityType = localityType;
        this.hasCustomaryName= hasCustomaryName;
        this.name = name;
        this.code = code;
        this.parentLocality = parentLocality;
        this.streets = streets;
    }

    public LocalityDistrict(LocalityType localityType, Boolean hasCustomaryName, String name, String code, Locality parentLocality) {
        this(null, localityType, hasCustomaryName, name, code, parentLocality, null);
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

        if (!(obj instanceof LocalityDistrict)) {
            return false;
        }

        LocalityDistrict otherLocalityDistrict = (LocalityDistrict) obj;
        return Objects.equals(otherLocalityDistrict.id, id) &&
                otherLocalityDistrict.code.equals(code) &&
                otherLocalityDistrict.name.equals(name) &&
                otherLocalityDistrict.localityType.equals(localityType) &&
                otherLocalityDistrict.hasCustomaryName.equals(hasCustomaryName) &&
                otherLocalityDistrict.parentLocality.equals(parentLocality);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, name, localityType, hasCustomaryName, parentLocality);
    }

}
