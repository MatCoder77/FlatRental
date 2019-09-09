package com.flatrental.domain.heatingtype;

import com.flatrental.api.SimpleResourceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/heatingtype")
public class HeatingTypeController {

    @Autowired
    private HeatingTypeService heatingTypeService;

    @GetMapping
    public List<SimpleResourceDTO> getHeatingTypes() {
        return heatingTypeService.getAllHeatingTypes().stream()
                .map(this::mapToSimpleResourceDTO)
                .collect(Collectors.toList());
    }

    private SimpleResourceDTO mapToSimpleResourceDTO(HeatingType heatingType) {
        return new SimpleResourceDTO(heatingType.getId(), heatingType.getName());
    }

}
