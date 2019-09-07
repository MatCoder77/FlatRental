package com.flatrental.domain.cookertype;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CookerTypeService {

    @Autowired
    private CookerTypeRepository cookerTypeRepository;

    public List<CookerType> getAllCookerTypes() {
        return cookerTypeRepository.findAll();
    }

}
