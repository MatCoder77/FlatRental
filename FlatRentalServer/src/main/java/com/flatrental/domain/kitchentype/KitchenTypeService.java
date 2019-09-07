package com.flatrental.domain.kitchentype;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KitchenTypeService {

    @Autowired
    private KitchenTypeRepository kitchenTypeRepository;

    public List<KitchenType> getAllKitchenTypes() {
        return kitchenTypeRepository.findAll();
    }

}
