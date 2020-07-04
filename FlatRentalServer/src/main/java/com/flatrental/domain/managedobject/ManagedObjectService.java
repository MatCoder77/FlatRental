package com.flatrental.domain.managedobject;

import com.flatrental.api.ManagedObjectDTO;
import com.flatrental.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ManagedObjectService {

    private final UserService userService;

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
