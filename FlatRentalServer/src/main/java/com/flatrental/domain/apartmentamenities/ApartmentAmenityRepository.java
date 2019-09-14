package com.flatrental.domain.apartmentamenities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApartmentAmenityRepository extends JpaRepository<ApartmentAmenity, Long> {
}
