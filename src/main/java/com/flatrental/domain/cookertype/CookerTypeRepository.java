package com.flatrental.domain.cookertype;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CookerTypeRepository extends JpaRepository<CookerType, Long> {

}
