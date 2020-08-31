package com.flatrental.domain.simpleattribute.windowtype;

import com.flatrental.domain.simpleattribute.SimpleAttribute;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "WindowTypes")
@Immutable
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "windowTypeCache")
public class WindowType extends SimpleAttribute {

}

