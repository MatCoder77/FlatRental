package com.flatrental.domain.userrole;

import com.flatrental.infrastructure.exceptions.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
public class UserRoleService {

    @Autowired
    UserRoleRepository accountTypeRepository;

    private static final String NO_SUCH_ACCOUNT_TYPE = "User role with name {0} not found";

    public UserRole getUserRoleFromDatabase(UserRoleName role) {
        return accountTypeRepository.findByName(role)
                .orElseThrow(() -> new AppException(MessageFormat.format(NO_SUCH_ACCOUNT_TYPE, role.toString())));
    }

}
