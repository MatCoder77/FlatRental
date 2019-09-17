package com.flatrental.domain.announcement.attributes;

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

}
