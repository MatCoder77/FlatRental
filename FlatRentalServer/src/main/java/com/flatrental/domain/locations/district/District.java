package com.flatrental.domain.locations.district;

import com.flatrental.domain.locations.voivodeship.Voivodeship;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Table(name = "Districts")
public class District {

    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    private Long id;

    @NotNull
    @ManyToOne
    private Voivodeship voivodeship;

    @NotNull
    @Size(min = 2, max = 2)
    @Column(length = 2)
    private String code;

    @NotNull
    @Column(length = 100)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private DistrictType type;

    public Long getId() {
        return id;
    }

    public Voivodeship getVoivodeship() {
        return voivodeship;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public DistrictType getType() {
        return type;
    }

    public void setVoivodeship(Voivodeship voivodeship) {
        this.voivodeship = voivodeship;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(DistrictType type) {
        this.type = type;
    }

    District(String code, String name, Voivodeship voivodeship, DistrictType type) {
        this.code = code;
        this.name= name;
        this.voivodeship = voivodeship;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof District)) {
            return false;
        }

        District otherDistrict = (District) o;
        return Objects.equals(otherDistrict.id, id) &&
                otherDistrict.code.equals(code) &&
                otherDistrict.name.equals(name) &&
                otherDistrict.type.equals(type) &&
                otherDistrict.voivodeship.equals(voivodeship);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, name, type, voivodeship);
    }

}
