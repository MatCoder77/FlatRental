package com.flatrental.domain.announcement.attributes.preferences;

import com.flatrental.api.SimpleResourceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PreferenceService {

    @Autowired
    private PreferenceRepository preferenceRepository;

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
