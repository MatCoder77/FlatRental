package com.flatrental.domain.locations.localitytype;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocalityTypeRepository extends JpaRepository<LocalityType, Long> {

    Optional<LocalityType> getLocalityTypeByTypeCode(String typeCode);

}
