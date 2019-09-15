package com.flatrental.domain.announcement.attributes.apartmentstate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApartmentStateRepository extends JpaRepository<ApartmentState, Long> {
}
