package com.flatrental.domain.authentication;

import com.flatrental.api.ChangeWithPasswordConfirmation;
import com.flatrental.api.LoginDTO;
import com.flatrental.api.RegistrationFormDTO;
import com.flatrental.api.ResourceDTO;
import com.flatrental.api.ResponseDTO;
import com.flatrental.api.TokenDTO;
import com.flatrental.domain.user.User;
import com.flatrental.domain.user.UserMapper;
import com.flatrental.infrastructure.security.HasAnyRole;
import com.flatrental.infrastructure.security.LoggedUser;
import com.flatrental.infrastructure.security.UserInfo;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(tags = "Authentication")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserMapper userMapper;

    @PostMapping("/signin")
    public TokenDTO authenticateUser(@Valid @RequestBody LoginDTO loginDTO) {
        String token = authenticationService.signInUser(loginDTO.getUsernameOrEmail(), loginDTO.getPassword());
        return new TokenDTO(token);
    }

    @PostMapping("/signup")
    public ResourceDTO registerUser(@Valid @RequestBody RegistrationFormDTO registrationFormDTO) {
        User userToCreate = userMapper.mapToUser(registrationFormDTO);
        User createdUser = authenticationService.registerUser(userToCreate);
        return userMapper.mapToResourceDTO(createdUser);
    }

    @PostMapping("/change-password")
    @HasAnyRole
    public ResponseDTO changePassword(@Valid @RequestBody ChangeWithPasswordConfirmation passwordChange, @LoggedUser UserInfo userInfo) {
        authenticationService.changeUserPassword(userInfo, passwordChange.getValue(), passwordChange.getPassword());
        return ResponseDTO.builder()
                .success(true)
                .build();
    }

    @PostMapping("/change-email")
    @HasAnyRole
    public ResponseDTO changeEmail(@Valid @RequestBody ChangeWithPasswordConfirmation emailChange, @LoggedUser UserInfo userInfo) {
        authenticationService.changeUserEmail(userInfo, emailChange.getValue(), emailChange.getPassword());
        return ResponseDTO.builder()
                .success(true)
                .build();
    }

}


