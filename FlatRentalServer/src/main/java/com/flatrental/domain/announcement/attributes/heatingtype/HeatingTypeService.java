package com.flatrental.domain.announcement.attributes.heatingtype;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HeatingTypeService {

    @Autowired
    private HeatingTypeRepository heatingTypeRepository;

    public List<HeatingType> getAllHeatingTypes() {
        return heatingTypeRepository.findAll();
    }

}
