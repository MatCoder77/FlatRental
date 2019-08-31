package com.flatrental.domain.locations.voivodeship;

import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Optional;

@Entity
@NoArgsConstructor
public class Voivodeship {

    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    private Long id;

    @NotNull
    @Column(length = 2, unique = true)
    private String code;

    @NotNull
    @Column(length = 100)
    private String name;

    Voivodeship(String code, String name) {
        this.name = name;
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Voivodeship)) {
            return false;
        }
        Voivodeship otherVoivodeship = (Voivodeship) obj;
        return Objects.equals(otherVoivodeship.id, id) &&
                otherVoivodeship.code.equals(code) &&
                otherVoivodeship.name.equals(name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, name);
    }

    @Override
    public String toString() {
        return Voivodeship.class.getSimpleName() + "{id = " + Optional.ofNullable(id).map(String::valueOf).orElse("null") + ", "
                + "code = " + code + ", name = " + name + "}";
    }

}
