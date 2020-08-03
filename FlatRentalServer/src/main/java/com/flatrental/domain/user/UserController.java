package com.flatrental.domain.user;

import com.flatrental.api.AvailableDTO;
import com.flatrental.api.ChangeDTO;
import com.flatrental.api.ResourceDTO;
import com.flatrental.api.ResponseDTO;
import com.flatrental.api.UserDTO;
import com.flatrental.domain.file.FileService;
import com.flatrental.infrastructure.security.HasAnyRole;
import com.flatrental.infrastructure.security.LoggedUser;
import com.flatrental.infrastructure.security.UserInfo;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.flatrental.infrastructure.utils.ResourcePaths.ID;
import static com.flatrental.infrastructure.utils.ResourcePaths.ID_PATH;

@Api(tags = "Users")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final FileService fileService;

    private static final String USERNAME = "username";
    private static final String EMAIL = "email";
    private static final String CHECK_USERNAME_PATH = "/check/username/{" + USERNAME + "}";
    private static final String CHECK_EMAIL_PATH = "/check/email/{" + EMAIL + "}";
    private static final String FILENAME = "filename";
    private static final String SET_AVATAR_PATH = "/set-avatar/{" + FILENAME + "}";

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

    @GetMapping(ID_PATH)
    @HasAnyRole
    public UserDTO getUser(@PathVariable(ID) Long id) {
        User user = userService.getExistingUser(id);
        return userService.mapToUserDTO(user);
    }

    @PostMapping(SET_AVATAR_PATH)
    @HasAnyRole
    public ResourceDTO setAvatar(@PathVariable(FILENAME) String filename, @LoggedUser UserInfo userInfo) {
        userService.setAvatar(filename, userInfo.getId());
        return ResourceDTO.builder()
                .uri(fileService.getDownloadUri(filename))
                .identifier(filename)
                .build();
    }

    @PostMapping("/change-phone")
    @HasAnyRole
    public ResponseDTO changePhoneNumber(@Valid @RequestBody ChangeDTO phoneNumberChange, @LoggedUser UserInfo userInfo) {
        User user = userService.getExistingUser(userInfo.getId());
        userService.setNewPhoneNumber(user, phoneNumberChange.getValue());
        return ResponseDTO.builder()
                .success(true)
                .build();
    }

    @PostMapping("/change-profile-description")
    @HasAnyRole
    public ResponseDTO changeProfileDescription(@Valid @RequestBody ChangeDTO profileDescriptionChanged, @LoggedUser UserInfo userInfo) {
        User user = userService.getExistingUser(userInfo.getId());
        userService.setUserProfileDescription(user, profileDescriptionChanged.getValue());
        return ResponseDTO.builder()
                .success(true)
                .build();
    }

}
