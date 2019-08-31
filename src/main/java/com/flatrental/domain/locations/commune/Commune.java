package com.flatrental.domain.locations.commune;

import com.flatrental.domain.locations.district.District;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
@NoArgsConstructor
public class Commune {

    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    Long id;

    @NotNull
    @ManyToOne
    private District district;

    @NotNull
    @Size(min = 2, max = 2)
    @Column(length = 2)
    private String code;

    @NotNull
    @Column(length = 100)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CommuneType type;


    public Commune(String code, String name, CommuneType type, District district) {
        this.code = code;
        this.name = name;
        this.type = type;
        this.district = district;
    }

    public Long getId() {
        return id;
    }

    public District getDistrict() {
        return district;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public CommuneType getType() {
        return type;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(CommuneType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Commune)) {
            return false;
        }

        Commune otherCommune = (Commune) obj;
        return Objects.equals(otherCommune.id, id) &&
                otherCommune.code.equals(code) &&
                otherCommune.name.equals(name) &&
                otherCommune.type.equals(type) &&
                otherCommune.district.equals(district);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, name, type, district);
    }

}
