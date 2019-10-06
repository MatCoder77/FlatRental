package com.flatrental.domain.announcement.simpleattributes.heatingtype;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeatingTypeRepository extends JpaRepository<HeatingType, Long> {


}
