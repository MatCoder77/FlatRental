package com.flatrental.domain.locations.abstractlocality;

import com.flatrental.domain.locations.commune.Commune;
import com.flatrental.domain.locations.street.Street;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AbstractLocalityRepository extends JpaRepository<AbstractLocality, Long> {

    Optional<AbstractLocality> findAbstractLocalityByCode(String code);

    Optional<AbstractLocality> findFirstByStreetsContains(Street street);

    Optional<AbstractLocality> findAbstractLocalityByCodeAndGenericLocalityType(String code, GenericLocalityType genericLocalityType);

    List<AbstractLocality> findAbstractLocalityByCommuneDistrictCodeAndGenericLocalityType(String districtCode, GenericLocalityType genericLocalityType);

    List<AbstractLocality> findAllByGenericLocalityType(GenericLocalityType genericLocalityType);

    List<AbstractLocality> findAbstractLocalityByParentLocalityAndGenericLocalityType(AbstractLocality parentLocality, GenericLocalityType genericLocalityType);

    List<AbstractLocality> findAbstractLocalityByCommuneAndGenericLocalityType(Commune commune, GenericLocalityType genericLocalityType);

    Optional<AbstractLocality> findAbstractLocalityByIdAndGenericLocalityType(Long id, GenericLocalityType genericLocalityType);

    List<AbstractLocality> findAbstractLocalitiesByLocalityDistrictAndGenericLocalityType(AbstractLocality localityDistrict, GenericLocalityType genericLocalityType);

}
