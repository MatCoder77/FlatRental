package com.flatrental.domain.announcement.simpleattribute.apartmentstate;

import com.flatrental.domain.announcement.simpleattribute.SimpleAttribute;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "ApartmentStates")
@Immutable
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "apartmentStateCache")
public class ApartmentState extends SimpleAttribute {

}
