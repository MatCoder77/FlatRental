package com.flatrental.domain.announcement.attributes.apartmentamenities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApartmentAmenityRepository extends JpaRepository<ApartmentAmenity, Long> {
}
