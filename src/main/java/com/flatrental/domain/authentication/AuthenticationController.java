package com.flatrental.domain.authentication;

import com.flatrental.api.LoginDTO;
import com.flatrental.api.RegistrationFormDTO;
import com.flatrental.api.ResourceDTO;
import com.flatrental.api.ResponseDTO;
import com.flatrental.api.UserDTO;
import com.flatrental.api.TokenDTO;
import com.flatrental.domain.user.User;
import com.flatrental.domain.user.UserService;
import com.flatrental.domain.userrole.UserRole;
import com.flatrental.domain.userrole.UserRoleName;
import com.flatrental.domain.userrole.UserRoleService;
import com.flatrental.infrastructure.security.TokenHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    TokenHandler tokenHandler;

    @Autowired
    private AuthenticationService authenticationService;

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
        return new ResourceDTO(uri);
    }

}


