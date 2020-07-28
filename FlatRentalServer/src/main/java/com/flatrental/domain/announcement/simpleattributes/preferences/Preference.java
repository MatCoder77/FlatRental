package com.flatrental.domain.announcement.simpleattributes.preferences;


import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@NoArgsConstructor
@Table(name = "Preferences")
@Immutable
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "preferenceCache")
public class Preference {

    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    private Long id;

    @NotBlank
    @NotNull
    private String name;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    private Preference(Long id) {
        this.id = id;
    }

    public static Preference fromId(Long id) {
        return new Preference(id);
    }

}
