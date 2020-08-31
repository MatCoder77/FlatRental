package com.flatrental.domain.simpleattribute.preferences;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PreferenceService {

    private final PreferenceRepository preferenceRepository;

    public List<Preference> getAllPreferences() {
        return preferenceRepository.findAll();
    }

    public List<Preference> getPreferences(Collection<Long> ids) {
        return preferenceRepository.findAllById(ids);
    }

}
