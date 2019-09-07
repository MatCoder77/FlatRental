package com.flatrental.domain.windowtype;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WindowTypeRepository extends JpaRepository<WindowType, Long> {


}
