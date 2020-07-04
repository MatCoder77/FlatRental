package com.flatrental.domain.announcement.simpleattributes.preferences;

import com.flatrental.api.SimpleResourceDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PreferenceService {

    private final PreferenceRepository preferenceRepository;

    public List<Preference> getAllPreferences() {
        return preferenceRepository.findAll();
    }

    public List<Preference> getPreferences(List<Long> ids) {
        return preferenceRepository.findAllById(ids);
    }

    public SimpleResourceDTO mapToSimpleResourceDTO(Preference preference) {
        return new SimpleResourceDTO(preference.getId(), preference.getName());
    }

}
