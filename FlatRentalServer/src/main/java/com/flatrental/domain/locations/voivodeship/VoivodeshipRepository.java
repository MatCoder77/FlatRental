package com.flatrental.domain.locations.voivodeship;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoivodeshipRepository extends JpaRepository<Voivodeship, Long> {

    Optional<Voivodeship> getVoivodeshipByCode(String code);

    void deleteVoivodeshipByCode(String code);

}
