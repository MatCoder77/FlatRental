package com.flatrental.domain.locations.abstractlocality;

import com.flatrental.domain.locations.commune.Commune;
import com.flatrental.domain.locations.locality.Locality;
import com.flatrental.domain.locations.localitydistrict.LocalityDistrict;
import com.flatrental.domain.locations.localitypart.LocalityPart;
import com.flatrental.domain.locations.localitytype.LocalityType;

import com.flatrental.domain.locations.street.Street;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Table(name = "AbstractLocalities")
public class AbstractLocality {

    @Id
    @GeneratedValue(generator = "TERYT_ID_GENERATOR")
    private Long id;

    @NotNull
    @ManyToOne
    private LocalityType localityType;

    @NotNull
    private Boolean hasCustomaryName;

    @NotNull
    @Column(length = 100)
    private String name;

    @NotNull
    @Size(min = 7, max = 7)
    @Column(length = 7, unique = true)
    private String code;

    @NotNull
    @Enumerated(EnumType.STRING)
    private GenericLocalityType genericLocalityType;

    @ManyToOne
    private Commune commune;

    @ManyToOne
    private AbstractLocality parentLocality;

    @ManyToOne
    private AbstractLocality localityDistrict;

    @ManyToMany
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(name = "AbstractLocalities_X_Streets")
    private Set<Street> streets = new HashSet<>();


    public static AbstractLocality fromLocality(Locality locality) {
        return AbstractLocality.builder()
                .withId(locality.getId())
                .withLocalityType(locality.getLocalityType())
                .withHasCustomaryName(locality.getHasCustomaryName())
                .withName(locality.getName())
                .withCode(locality.getCode())
                .withGenericLocalityType(GenericLocalityType.AUTONOMOUS_LOCALITY)
                .withCommune(locality.getCommune())
                .build();
    }

    public static AbstractLocality fromLocalityDistrict(LocalityDistrict localityDistrict) {
        return AbstractLocality.builder()
                .withId(localityDistrict.getId())
                .withLocalityType(localityDistrict.getLocalityType())
                .withHasCustomaryName(localityDistrict.getHasCustomaryName())
                .withName(localityDistrict.getName())
                .withCode(localityDistrict.getCode())
                .withGenericLocalityType(GenericLocalityType.LOCALITY_DISTRICT)
                .withParentLocality(AbstractLocality.fromLocality(localityDistrict.getParentLocality()))
                .build();
    }

    public static AbstractLocality fromLocalityPart(LocalityPart localityPart) {
        return AbstractLocality.builder()
                .withId(localityPart.getId())
                .withLocalityType(localityPart.getLocalityType())
                .withHasCustomaryName(localityPart.getHasCustomaryName())
                .withName(localityPart.getName())
                .withCode(localityPart.getCode())
                .withGenericLocalityType(GenericLocalityType.LOCALITY_PART)
                .withParentLocality(AbstractLocality.fromLocality(localityPart.getParentLocality()))
                .withLocalityDistrict(localityPart.getLocalityDistrict().map(AbstractLocality::fromLocalityDistrict).orElse(null))
                .build();
    }

    private AbstractLocality(Builder builder) {
        this.id = builder.id;
        this.localityType = builder.localityType;
        this.hasCustomaryName = builder.hasCustomaryName;
        this.name = builder.name;
        this.code = builder.code;
        this.genericLocalityType = builder.genericLocalityType;
        this.commune = builder.commune;
        this.parentLocality = builder.parentLocality;
        this.localityDistrict = builder.localityDistrict;
        this.streets = builder.streets;
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

    public GenericLocalityType getGenericLocalityType() {
        return genericLocalityType;
    }

    public void setGenericLocalityType(GenericLocalityType genericLocalityType) {
        this.genericLocalityType = genericLocalityType;
    }

    public Commune getCommune() {
        return commune;
    }

    public void setCommune(Commune commune) {
        this.commune = commune;
    }

    public AbstractLocality getParentLocality() {
        return parentLocality;
    }

    public void setParentLocality(AbstractLocality parentLocality) {
        this.parentLocality = parentLocality;
    }

    public AbstractLocality getLocalityDistrict() {
        return localityDistrict;
    }

    public void setLocalityDistrict(AbstractLocality localityDistrict) {
        this.localityDistrict = localityDistrict;
    }

    public Set<Street> getStreets() {
        return streets;
    }

    public void addStreet(Street street) {
        this.streets.add(street);
    }

    public void addStreets(Set<Street> streetsToAdd) {
        this.streets.addAll(streetsToAdd);
    }

    public void removeStreet(Street street) {
        this.streets.remove(street);
    }

    public void removeStreets(Set<Street> streetsToRemove) {
        this.streets.removeAll(streetsToRemove);
    }

    private static Builder builder() {
        return new Builder();
    }

    private static class Builder {

        private Long id;
        private LocalityType localityType;
        private Boolean hasCustomaryName;
        private String name;
        private String code;
        private GenericLocalityType genericLocalityType;
        private Commune commune;
        private AbstractLocality parentLocality;
        private AbstractLocality localityDistrict;
        private Set<Street> streets = new HashSet<>();

        private Builder withId(Long id) {
            this.id = id;
            return this;
        }

        private Builder withLocalityType(LocalityType localityType) {
            this.localityType = localityType;
            return this;
        }

        private Builder withHasCustomaryName(Boolean hasCustomaryName) {
            this.hasCustomaryName = hasCustomaryName;
            return this;
        }

        private Builder withName(String name) {
            this.name = name;
            return this;
        }

        private Builder withCode(String code) {
            this.code = code;
            return this;
        }

        private Builder withGenericLocalityType(GenericLocalityType genericLocalityType) {
            this.genericLocalityType = genericLocalityType;
            return this;
        }

        private Builder withCommune(Commune commune) {
            this.commune = commune;
            return this;
        }

        private Builder withParentLocality(AbstractLocality parentLocality) {
            this.parentLocality = parentLocality;
            return this;
        }

        private Builder withLocalityDistrict(AbstractLocality localityDistrict) {
            this.localityDistrict = localityDistrict;
            return this;
        }

        private AbstractLocality build() {
            return new AbstractLocality(this);
        }

    }

}
