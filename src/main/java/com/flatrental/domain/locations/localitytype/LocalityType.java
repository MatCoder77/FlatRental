package com.flatrental.domain.locations.localitytype;

import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
@NoArgsConstructor
public class LocalityType {

    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    Long id;

    @NotNull
    @Size(min = 2, max = 2)
    @Column(length = 2, unique = true)
    private String typeCode;

    @NotNull
    @Column(length = 100)
    private String typeName;

    public LocalityType(@NotNull String typeCode, @NotNull String typeName) {
        this.typeCode = typeCode;
        this.typeName = typeName;
    }

    public Long getId() {
        return id;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public String getTypeName() {
        return typeName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof LocalityType)) {
            return false;
        }

        LocalityType otherLocalityType = (LocalityType) obj;
        return Objects.equals(otherLocalityType.id, id) &&
                otherLocalityType.typeCode.equals(typeCode) &&
                otherLocalityType.typeName.equals(typeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, typeCode, typeName);
    }

}
