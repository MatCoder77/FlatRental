package com.flatrental.domain.simpleattribute.preferences;


import com.flatrental.domain.simpleattribute.SimpleAttribute;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Preferences")
@Immutable
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "preferenceCache")
@NoArgsConstructor
public class Preference extends SimpleAttribute {

    public Preference(Long id) {
        super(id);
    }

}
