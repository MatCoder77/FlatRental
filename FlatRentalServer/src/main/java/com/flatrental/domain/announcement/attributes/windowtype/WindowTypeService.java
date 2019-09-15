package com.flatrental.domain.announcement.attributes.windowtype;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WindowTypeService {

    @Autowired
    private WindowTypeRepository windowTypeRepository;

    public List<WindowType> getAllWindowTypes() {
        return windowTypeRepository.findAll();
    }

}
