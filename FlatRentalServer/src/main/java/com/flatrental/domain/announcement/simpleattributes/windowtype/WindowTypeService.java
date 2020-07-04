package com.flatrental.domain.announcement.simpleattributes.windowtype;

import com.flatrental.api.SimpleResourceDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WindowTypeService {

    private final WindowTypeRepository windowTypeRepository;

    private static final String NOT_FOUND = "There is no WindowType with id {0}";

    public List<WindowType> getAllWindowTypes() {
        return windowTypeRepository.findAll();
    }

    public WindowType getExistingWindowType(Long id) {
        return windowTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(MessageFormat.format(NOT_FOUND, id)));
    }

    public SimpleResourceDTO mapToSimpleResourceDTO(WindowType windowType) {
        return new SimpleResourceDTO(windowType.getId(), windowType.getName());
    }

}
