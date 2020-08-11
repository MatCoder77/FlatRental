package com.flatrental.domain.authentication;

import com.flatrental.domain.user.User;
import com.flatrental.domain.user.UserService;
import com.flatrental.infrastructure.security.TokenHandler;
import com.flatrental.infrastructure.security.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final TokenHandler tokenHandler;
    private final UserService userService;

    public User registerUser(User user) {
        return userService.createUser(user);
    }

    public String signInUser(String usernameOrEmail, String password) {
        Authentication authentication = authenticateUser(usernameOrEmail, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return tokenHandler.generateToken(authentication);
    }

    public Authentication authenticateUser(String usernameOrEmail, String password) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(usernameOrEmail, password);
        return authenticationManager.authenticate(token);
    }

    public void changeUserPassword(UserInfo userInfo, String newPassword, String password) {
        authenticateUser(userInfo.getUsername(), password);
        userService.updatePassword(userInfo.getId(), newPassword);
    }

    public void changeUserEmail(UserInfo userInfo, String newEmail, String password) {
        authenticateUser(userInfo.getUsername(), password);
        userService.updateEmail(userInfo.getId(), newEmail);
    }

}
