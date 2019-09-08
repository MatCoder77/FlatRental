package com.flatrental.domain.user;

import com.flatrental.api.AvailableDTO;
import com.flatrental.api.UserDTO;
import com.flatrental.infrastructure.security.LoggedUser;
import com.flatrental.infrastructure.security.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    private static final String USERNAME = "username";
    private static final String EMAIL = "email";
    private static final String CHECK_USERNAME_PATH = "/check/username/{" + USERNAME + "}";
    private static final String CHECK_EMAIL_PATH = "/check/email/{" + EMAIL + "}";

    @GetMapping(CHECK_USERNAME_PATH)
    public AvailableDTO checkUsernameAvailable(@PathVariable(USERNAME) String username) {
        Boolean isAvailable = !userService.userExistsByUsername(username);
        return new AvailableDTO(isAvailable);
    }

    @GetMapping(CHECK_EMAIL_PATH)
    public AvailableDTO checkIfEmailAvailable(@PathVariable(EMAIL) String email) {
        Boolean isAvailable = !userService.userExistsByEmail(email);
        return new AvailableDTO(isAvailable);
    }

    @GetMapping("/current")
    @PreAuthorize("hasRole('USER')")
    public UserDTO getCurrentUser(@LoggedUser UserInfo currentUserInfo) {
        User user = userService.getUserFormDatabase(currentUserInfo.getId());
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .username(user.getUsername())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

}
