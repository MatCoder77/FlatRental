package com.flatrental.domain.simpleattribute.buildingmaterial;

import com.flatrental.domain.simpleattribute.SimpleAttribute;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "BuildingMaterials")
@Immutable
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "buildingMaterialCache")
public class BuildingMaterial extends SimpleAttribute {

}
