package com.flatrental.domain.locations.locality;


import com.flatrental.domain.locations.abstractlocality.AbstractLocality;
import com.flatrental.domain.locations.abstractlocality.GenericLocalityType;
import com.flatrental.domain.locations.commune.Commune;
import com.flatrental.domain.locations.localitytype.LocalityType;
import com.flatrental.domain.locations.street.Street;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.text.MessageFormat;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class Locality {

    private Long id;

    @NotNull
    private LocalityType localityType;

    @NotNull
    private Boolean hasCustomaryName;

    @NotNull
    @Size(min = 1, max = 100)
    private String name;

    @NotNull
    @Size(min = 7, max = 7)
    private String code;

    @NotNull
    private Commune commune;

    private Set<Street> streets;

    public static Locality fromAbstractLocality(AbstractLocality abstractLocality) {
        validateGenericType(abstractLocality);
        return new Locality(
                abstractLocality.getId(),
                abstractLocality.getLocalityType(),
                abstractLocality.getHasCustomaryName(),
                abstractLocality.getName(),
                abstractLocality.getCode(),
                abstractLocality.getCommune(),
                abstractLocality.getStreets());
    }

    private static void validateGenericType(AbstractLocality abstractLocality) {
        if (abstractLocality.getGenericLocalityType() != GenericLocalityType.AUTONOMOUS_LOCALITY) {
            throw new IllegalArgumentException(MessageFormat.format("Cannot convert AbstractLocality with generic type {0} to Locality", abstractLocality.getGenericLocalityType()));
        }
    }

    public Locality(Long id, LocalityType localityType, Boolean hasCustomaryName, String name, String code, Commune commune, Set<Street> streets) {
        this(localityType, hasCustomaryName, name, code, commune);
        this.id = id;
        this.streets = streets;
    }

    public Locality(LocalityType localityType, Boolean hasCustomaryName, String name, String code, Commune commune) {
        this.localityType = localityType;
        this.hasCustomaryName = hasCustomaryName;
        this.name = name;
        this.code = code;
        this.commune = commune;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Commune getCommune() {
        return commune;
    }

    public void setCommune(Commune commune) {
        this.commune = commune;
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

        if (!(obj instanceof Locality)) {
            return false;
        }

        Locality otherLocality = (Locality) obj;
        return Objects.equals(otherLocality.id, id) &&
                otherLocality.code.equals(code) &&
                otherLocality.name.equals(name) &&
                otherLocality.hasCustomaryName.equals(hasCustomaryName) &&
                otherLocality.localityType.equals(localityType) &&
                otherLocality.commune.equals(commune);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, name, hasCustomaryName, localityType, commune);
    }

    @Override
    public String toString() {
        return "Locality {" + "id = " + Optional.ofNullable(id).map(String::valueOf).orElse("null")
                + ", localityType = " + localityType
                + ", hasCustomaryName = " + hasCustomaryName
                + ", name = " + name
                + ", commune id = " + commune.getId()
                + "}";
    }

}
