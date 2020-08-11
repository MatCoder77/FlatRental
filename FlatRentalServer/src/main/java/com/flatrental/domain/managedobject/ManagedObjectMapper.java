package com.flatrental.domain.managedobject;

import com.flatrental.api.ManagedObjectDTO;
import com.flatrental.domain.user.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ManagedObjectMapper {

    private final UserMapper userMapper;

    public ManagedObjectDTO mapToManagedObjectDTO(ManagedObject managedObject) {
        return ManagedObjectDTO.builder()
                .createdAt(managedObject.getCreatedAt())
                .createdBy(userMapper.mapToUserDTO(managedObject.getCreatedBy()))
                .updatedAt(managedObject.getUpdatedAt())
                .updatedBy(userMapper.mapToUserDTO(managedObject.getUpdatedBy()))
                .objectState(managedObject.getObjectState().name())
                .build();
    }

}
