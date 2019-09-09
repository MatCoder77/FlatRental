package com.flatrental.domain.windowtype;

import com.flatrental.api.SimpleResourceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/windowtype")
public class WindowController {

    @Autowired
    private WindowTypeService windowTypeService;

    @GetMapping
    public List<SimpleResourceDTO> getWindowTypes() {
        return windowTypeService.getAllWindowTypes().stream()
                .map(this::mapToSimpleResourceDTO)
                .collect(Collectors.toList());
    }

    private SimpleResourceDTO mapToSimpleResourceDTO(WindowType windowType) {
        return new SimpleResourceDTO(windowType.getId(), windowType.getName());
    }

}
