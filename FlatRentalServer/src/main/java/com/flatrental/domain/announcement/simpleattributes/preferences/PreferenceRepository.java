package com.flatrental.domain.announcement.simpleattributes.preferences;


import com.flatrental.domain.announcement.simpleattributes.SimpleAttributeCacheableJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreferenceRepository extends SimpleAttributeCacheableJpaRepository<Preference, Long> {

}
