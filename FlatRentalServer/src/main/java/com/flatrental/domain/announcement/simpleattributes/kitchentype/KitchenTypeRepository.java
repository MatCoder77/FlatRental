package com.flatrental.domain.announcement.simpleattributes.kitchentype;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KitchenTypeRepository extends JpaRepository<KitchenType, Long> {

}
