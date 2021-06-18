package com.flatrental.domain.user;

import com.flatrental.api.user.AvailableDTO;
import com.flatrental.api.authentication.ChangeDTO;
import com.flatrental.api.common.ResourceDTO;
import com.flatrental.api.common.ResponseDTO;
import com.flatrental.api.user.UserDTO;
import com.flatrental.infrastructure.security.HasAnyRole;
import com.flatrental.infrastructure.security.LoggedUser;
import com.flatrental.infrastructure.security.UserInfo;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.flatrental.infrastructure.rest.ResourcePaths.ID;
import static com.flatrental.infrastructure.rest.ResourcePaths.ID_PATH;

@Api(tags = "Users")
@RestController
@RequestMapping(UserController.MAIN_RESOURCE)
@RequiredArgsConstructor
public class UserController {

    public static final String MAIN_RESOURCE = "/api/user";
    private static final String USERNAME_PATH_PARAM = "username";
    private static final String EMAIL_PATH_PARAM = "email";
    private static final String CHECK_USERNAME_RESOURCE = "/check/username/{" + USERNAME_PATH_PARAM + "}";
    private static final String CHECK_EMAIL_RESOURCE = "/check/email/{" + EMAIL_PATH_PARAM + "}";
    private static final String FILENAME_PATH_PARAM = "filename";
    private static final String SET_AVATAR_RESOURCE = "/set-avatar/{" + FILENAME_PATH_PARAM + "}";

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping(CHECK_USERNAME_RESOURCE)
    public AvailableDTO checkUsernameAvailable(@PathVariable(USERNAME_PATH_PARAM) String username) {
        Boolean isAvailable = !userService.userExistsByUsername(username);
        return new AvailableDTO(isAvailable);
    }

    @GetMapping(CHECK_EMAIL_RESOURCE)
    public AvailableDTO checkIfEmailAvailable(@PathVariable(EMAIL_PATH_PARAM) String email) {
        Boolean isAvailable = !userService.userExistsByEmail(email);
        return new AvailableDTO(isAvailable);
    }

    @GetMapping("/current")
    @HasAnyRole
    public UserDTO getCurrentUser(@LoggedUser UserInfo currentUserInfo) {
        User user = userService.getExistingUser(currentUserInfo.getId());
        return userMapper.mapToUserDTO(user);
    }

    @GetMapping(ID_PATH)
    @HasAnyRole
    public UserDTO getUser(@PathVariable(ID) Long id) {
        User user = userService.getExistingUser(id);
        return userMapper.mapToUserDTO(user);
    }

    @PutMapping(SET_AVATAR_RESOURCE)
    @HasAnyRole
    public ResourceDTO setAvatar(@PathVariable(FILENAME_PATH_PARAM) String filename, @LoggedUser UserInfo userInfo) {
        userService.updateAvatar(userInfo.getId(), filename);
        return ResourceDTO.builder()
                .uri(userMapper.mapToAvatarUrl(filename))
                .identifier(filename)
                .build();
    }

    @PutMapping("/change-phone")
    @HasAnyRole
    public ResponseDTO changePhoneNumber(@Valid @RequestBody ChangeDTO phoneNumberChange, @LoggedUser UserInfo userInfo) {
        userService.updatePhoneNumber(userInfo.getId(), phoneNumberChange.getValue());
        return ResponseDTO.builder()
                .success(true)
                .build();
    }

    @PutMapping("/change-profile-description")
    @HasAnyRole
    public ResponseDTO changeProfileDescription(@Valid @RequestBody ChangeDTO profileDescriptionChanged, @LoggedUser UserInfo userInfo) {
        userService.updateAbout(userInfo.getId(), profileDescriptionChanged.getValue());
        return ResponseDTO.builder()
                .success(true)
                .build();
    }

}
