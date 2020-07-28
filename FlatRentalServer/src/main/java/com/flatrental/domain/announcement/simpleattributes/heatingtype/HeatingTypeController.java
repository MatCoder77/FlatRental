package com.flatrental.domain.announcement.simpleattributes.heatingtype;

import com.flatrental.api.SimpleResourceDTO;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "Reference Attributes")
@RestController
@RequestMapping("/api/heatingtype")
@RequiredArgsConstructor
public class HeatingTypeController {

    private final HeatingTypeService heatingTypeService;

    @GetMapping
    public List<SimpleResourceDTO> getHeatingTypes() {
        return heatingTypeService.getAllHeatingTypes().stream()
                .map(heatingTypeService::mapToSimpleResourceDTO)
                .collect(Collectors.toList());
    }

}
