package com.flatrental.domain.locations.district;

import com.flatrental.domain.locations.voivodeship.Voivodeship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DistrictRepository extends JpaRepository<District, Long> {

    Optional<District> getDistrictByCodeAndVoivodeshipCode(String districtCode, String voivodeshipCode);

    List<District> getAllByVoivodeship(Voivodeship voivodeship);

}
