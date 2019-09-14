package com.flatrental.domain.locations.commune;

import com.flatrental.domain.locations.district.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommuneRepository extends JpaRepository<Commune, Long> {

    Optional<Commune> getCommuneByCodeAndDistrictId(String communeCode, Long districtId);

    List<Commune> getCommunesByDistrict(District district);

}
