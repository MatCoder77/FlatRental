package com.flatrental.domain.announcement.attributes.cookertype;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CookerTypeRepository extends JpaRepository<CookerType, Long> {

}
