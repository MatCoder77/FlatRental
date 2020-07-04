package com.flatrental.domain.announcement.simpleattributes.preferences;

import com.flatrental.api.SimpleResourceDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/preferences")
@RequiredArgsConstructor
public class PreferenceController {

    private final PreferenceService preferenceService;

    @GetMapping
    public List<SimpleResourceDTO> getAllPreferences() {
        return preferenceService.getAllPreferences()
                .stream()
                .map(preferenceService::mapToSimpleResourceDTO)
                .collect(Collectors.toList());
    }

}
