package com.flatrental.domain.simpleattribute.preferences;


import com.flatrental.domain.simpleattribute.SimpleAttributeCacheableJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreferenceRepository extends SimpleAttributeCacheableJpaRepository<Preference, Long> {

}
