package com.flatrental.domain.announcement.simpleattribute.preferences;


import com.flatrental.domain.announcement.simpleattribute.SimpleAttributeCacheableJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreferenceRepository extends SimpleAttributeCacheableJpaRepository<Preference, Long> {

}
