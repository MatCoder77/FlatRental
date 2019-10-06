package com.flatrental.domain.announcement.simpleattributes.preferences;

import com.flatrental.api.SimpleResourceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/preferences")
public class PreferenceController {

    @Autowired
    private PreferenceService preferenceService;

    @GetMapping
    public List<SimpleResourceDTO> getAllPreferences() {
        return preferenceService.getAllPreferences()
                .stream()
                .map(preferenceService::mapToSimpleResourceDTO)
                .collect(Collectors.toList());
    }

}
