package com.flatrental.domain.authentication;

import com.flatrental.api.ChangeWithPasswordConfirmation;
import com.flatrental.api.LoginDTO;
import com.flatrental.api.RegistrationFormDTO;
import com.flatrental.api.ResourceDTO;
import com.flatrental.api.ResponseDTO;
import com.flatrental.api.TokenDTO;
import com.flatrental.domain.user.User;
import com.flatrental.domain.user.UserService;
import com.flatrental.infrastructure.security.HasAnyRole;
import com.flatrental.infrastructure.security.LoggedUser;
import com.flatrental.infrastructure.security.TokenHandler;
import com.flatrental.infrastructure.security.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final TokenHandler tokenHandler;
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("/signin")
    public TokenDTO authenticateUser(@Valid @RequestBody LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(authenticationService.getAuthenticationToken(loginDTO));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenHandler.generateToken(authentication);
        return authenticationService.mapToTokenDTO(token);
    }

    @PostMapping("/signup")
    public ResourceDTO registerUser(@Valid @RequestBody RegistrationFormDTO registrationFormDTO) {
        User newUser = authenticationService.createUserBasedOnRegistrationForm(registrationFormDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/users/{username}")
                .buildAndExpand(newUser.getUsername())
                .toUri();
        return ResourceDTO.builder()
                .id(newUser.getId())
                .uri(uri)
                .build();
    }

    @PostMapping("/change-password")
    @HasAnyRole
    public ResponseDTO changePassword(@Valid @RequestBody ChangeWithPasswordConfirmation passwordChange, @LoggedUser UserInfo userInfo) {
        authenticationManager.authenticate(authenticationService.getAuthenticationToken(userInfo.getUsername(), passwordChange.getPassword()));
        User user = userService.getExistingUser(userInfo.getId());
        userService.setNewPassword(user, passwordChange.getValue());
        return ResponseDTO.builder()
                .success(true)
                .build();
    }

    @PostMapping("/change-email")
    @HasAnyRole
    public ResponseDTO changeEmail(@Valid @RequestBody ChangeWithPasswordConfirmation emailChange, @LoggedUser UserInfo userInfo) {
        authenticationManager.authenticate(authenticationService.getAuthenticationToken(userInfo.getUsername(), emailChange.getPassword()));
        User user = userService.getExistingUser(userInfo.getId());
        userService.setNewEmail(user, emailChange.getValue());
        return ResponseDTO.builder()
                .success(true)
                .build();
    }

}


