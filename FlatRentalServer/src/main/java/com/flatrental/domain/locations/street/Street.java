package com.flatrental.domain.locations.street;

import com.flatrental.domain.locations.teryt.ulic.StreetType;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;
import java.util.Optional;

@Entity
@NoArgsConstructor
@Table(name = "Streets")
public class Street {

    @Id
    @GeneratedValue(generator = "TERYT_ID_GENERATOR")
    private Long id;

    @NotNull
    @Size(min = 5, max = 5)
    @Column(length = 5)
    private String code;

    @NotNull
    @Enumerated(EnumType.STRING)
    private StreetType streetType;

    @NotNull
    @Column(length = 100)
    private String mainName;

    @Column(length = 100)
    private String leadingName;


    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public StreetType getStreetType() {
        return streetType;
    }

    public String getMainName() {
        return mainName;
    }

    public Optional<String> getLeadingName() {
        return Optional.ofNullable(leadingName);
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setStreetType(StreetType streetType) {
        this.streetType = streetType;
    }

    public void setMainName(String mainName) {
        this.mainName = mainName;
    }

    public void setLeadingName(String leadingName) {
        this.leadingName = leadingName;
    }

    public Street(@NotNull String code, @NotNull StreetType streetType, @NotNull String mainName, String leadingName) {
        this.code = code;
        this.streetType = streetType;
        this.mainName = mainName;
        this.leadingName = leadingName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Street)) {
            return false;
        }
        Street otherStreet = (Street) obj;
        return Objects.equals(otherStreet.id, id) &&
                otherStreet.code.equals(code) &&
                otherStreet.streetType.equals(streetType) &&
                otherStreet.mainName.equals(mainName) &&
                otherStreet.leadingName.equals(leadingName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, streetType, mainName, leadingName);
    }

}
