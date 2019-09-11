package com.flatrental.domain.apartmentstate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApartmentStateService {

    @Autowired
    private ApartmentStateRepository apartmentStateRepository;

    public List<ApartmentState> getAllApartmentStateTypes() {
        return apartmentStateRepository.findAll();
    }

}
