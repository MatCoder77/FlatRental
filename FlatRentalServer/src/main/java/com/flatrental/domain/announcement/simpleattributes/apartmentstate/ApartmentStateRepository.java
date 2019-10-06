package com.flatrental.domain.announcement.simpleattributes.apartmentstate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApartmentStateRepository extends JpaRepository<ApartmentState, Long> {
}
