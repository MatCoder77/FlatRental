package com.flatrental.domain.parkingtype;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingTypeRepository extends JpaRepository<ParkingType, Long> {

}
