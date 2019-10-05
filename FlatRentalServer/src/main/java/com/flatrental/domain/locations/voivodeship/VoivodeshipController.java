package com.flatrental.domain.locations.voivodeship;

import com.flatrental.api.VoivodeshipDTO;
import com.flatrental.infrastructure.security.HasAnyRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/voivodeship")
public class VoivodeshipController {

    @Autowired
    private VoivodeshipService voivodeshipService;

    @HasAnyRole
    @GetMapping
    public List<VoivodeshipDTO> getVoivodeships() {
        return voivodeshipService.getAllVoivodeships().stream()
                .map(voivodeshipService::mapToVoivodeshipDTO)
                .collect(Collectors.toList());
    }

}
