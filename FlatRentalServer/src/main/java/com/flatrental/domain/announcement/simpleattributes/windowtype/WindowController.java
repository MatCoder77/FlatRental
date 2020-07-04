package com.flatrental.domain.announcement.simpleattributes.windowtype;

import com.flatrental.api.SimpleResourceDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/windowtype")
@RequiredArgsConstructor
public class WindowController {

    private final WindowTypeService windowTypeService;

    @GetMapping
    public List<SimpleResourceDTO> getWindowTypes() {
        return windowTypeService.getAllWindowTypes().stream()
                .map(windowTypeService::mapToSimpleResourceDTO)
                .collect(Collectors.toList());
    }

}
