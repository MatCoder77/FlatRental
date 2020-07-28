package com.flatrental.domain.announcement.simpleattributes.cookertype;

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
@RequestMapping("/api/cookertype")
@RequiredArgsConstructor
public class CookerTypeController {

    private final CookerTypeService cookerTypeService;

    @GetMapping
    public List<SimpleResourceDTO> getCookerTypes() {
        return cookerTypeService.getAllCookerTypes().stream()
                .map(cookerTypeService::mapToSimpleResourceDTO)
                .collect(Collectors.toList());
    }



}
