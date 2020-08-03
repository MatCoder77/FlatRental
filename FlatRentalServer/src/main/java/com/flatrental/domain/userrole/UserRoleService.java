package com.flatrental.domain.userrole;

import com.flatrental.infrastructure.exceptions.IllegalArgumentAppException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
@RequiredArgsConstructor
public class UserRoleService {

    private final UserRoleRepository accountTypeRepository;

    private static final String NO_SUCH_ACCOUNT_TYPE = "User role with name {0} not found";

    public UserRole getExistingUserRole(UserRoleName role) {
        return accountTypeRepository.findByName(role)
                .orElseThrow(() -> new IllegalArgumentAppException(MessageFormat.format(NO_SUCH_ACCOUNT_TYPE, role.toString())));
    }

}
