package com.flatrental.domain.announcement.simpleattribute.parkingtype;

import com.flatrental.domain.announcement.simpleattribute.SimpleAttribute;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "ParkingTypes")
@Immutable
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "parkingTypeCache")
public class ParkingType extends SimpleAttribute {

}
