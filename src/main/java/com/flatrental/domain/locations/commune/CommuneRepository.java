package com.flatrental.domain.locations.commune;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommuneRepository extends JpaRepository<Commune, Long> {

    Optional<Commune> getCommuneByCodeAndDistrictId(String communeCode, Long districtId);

}
