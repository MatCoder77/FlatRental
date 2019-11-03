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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Calendar;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    TokenHandler tokenHandler;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserService userService;

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    public static final String CONFIRM_REGISTRATION_PATH = "/confirm-registration";
    public static final String TOKEN = "token";

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
        applicationEventPublisher.publishEvent(new RegistrationConfirmationEvent(newUser));
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/users/{username}")
                .buildAndExpand(newUser.getUsername())
                .toUri();
        return ResourceDTO.builder()
                .id(newUser.getId())
                .uri(uri)
                .build();
    }

    @GetMapping(CONFIRM_REGISTRATION_PATH)
    public String confirmRegistration(@RequestParam(TOKEN) String token) {
        VerificationToken verificationToken = authenticationService.getVerificationToken(token);
        if (verificationToken == null) {
            return "redirect:/badUser.html";
        }
        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            return "redirect:/badUser.html";
        }
        userService.activateUserAccount(user);
        return "redirect:http://localhost:3000/login";
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


