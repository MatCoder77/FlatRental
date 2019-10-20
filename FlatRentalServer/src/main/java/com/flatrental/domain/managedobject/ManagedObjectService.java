package com.flatrental.domain.managedobject;

import com.flatrental.api.ManagedObjectDTO;
import com.flatrental.domain.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManagedObjectService {

    @Autowired
    private UserService userService;

    public ManagedObjectDTO mapToManagedObjectDTO(ManagedObject managedObject) {
        return ManagedObjectDTO.builder()
                .createdAt(managedObject.getCreatedAt())
                .createdBy(userService.mapToUserDTO(managedObject.getCreatedBy()))
                .updatedAt(managedObject.getUpdatedAt())
                .updatedBy(userService.mapToUserDTO(managedObject.getUpdatedBy()))
                .objectState(managedObject.getObjectState().name())
                .build();
    }

}
