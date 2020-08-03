package com.flatrental.domain.authentication;

import com.flatrental.api.LoginDTO;
import com.flatrental.api.RegistrationFormDTO;
import com.flatrental.api.TokenDTO;
import com.flatrental.domain.statistics.user.UserStatistics;
import com.flatrental.domain.user.User;
import com.flatrental.domain.user.UserService;
import com.flatrental.domain.userrole.UserRole;
import com.flatrental.domain.userrole.UserRoleName;
import com.flatrental.domain.userrole.UserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final UserRoleService userRoleService;

    public UsernamePasswordAuthenticationToken getAuthenticationToken(LoginDTO loginDTO) {
        return new UsernamePasswordAuthenticationToken(loginDTO.getUsernameOrEmail(), loginDTO.getPassword());
    }

    public UsernamePasswordAuthenticationToken getAuthenticationToken(String username, String password) {
        return new UsernamePasswordAuthenticationToken(username, password);
    }

    public TokenDTO mapToTokenDTO(String token) {
        return new TokenDTO(token);
    }

    public User createUserBasedOnRegistrationForm(RegistrationFormDTO registrationFormDTO) {
        UserRole userRole = userRoleService.getExistingUserRole(UserRoleName.ROLE_USER);
        User newUser = new User(null,
                registrationFormDTO.getName(),
                registrationFormDTO.getSurname(),
                registrationFormDTO.getUsername(),
                registrationFormDTO.getPassword(),
                registrationFormDTO.getEmail(),
                registrationFormDTO.getPhoneNumber(),
                null,
                null,
                new UserStatistics(),
                Collections.singleton(userRole),
                null);
        return userService.registerUser(newUser);
    }

}
