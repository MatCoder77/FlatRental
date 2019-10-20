package com.flatrental.domain.user;

import com.flatrental.api.AvailableDTO;
import com.flatrental.api.FileDTO;
import com.flatrental.api.FileUploadDTO;
import com.flatrental.api.ResourceDTO;
import com.flatrental.api.ResponseDTO;
import com.flatrental.api.UserDTO;
import com.flatrental.domain.file.FileController;
import com.flatrental.infrastructure.security.HasAnyRole;
import com.flatrental.infrastructure.security.LoggedUser;
import com.flatrental.infrastructure.security.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


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
    @HasAnyRole
    public UserDTO getCurrentUser(@LoggedUser UserInfo currentUserInfo) {
        User user = userService.getExistingUser(currentUserInfo.getId());
        return userService.mapToUserDTO(user);
    }

    @PostMapping("/set-avatar")
    @HasAnyRole
    public ResponseDTO setAvatar(String filename, @LoggedUser UserInfo userInfo) {
        userService.setAvatar(filename, userInfo.getId());
        return ResponseDTO.builder()
                .success(true)
                .message("Avatar updated successfully")
                .build();
    }

}
