package com.flatrental.domain.permissions;

import com.flatrental.domain.announcement.Announcement;
import com.flatrental.domain.user.User;
import com.flatrental.domain.user.UserService;
import com.flatrental.domain.userrole.UserRole;
import com.flatrental.domain.userrole.UserRoleName;
import com.flatrental.infrastructure.security.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
public class PermissionsValidationService {

    @Autowired
    private UserService userService;

    private static final String PERMISSION_DENIED_TO_EDIT_ANNOUNCEMENT = "You have no permission to edit announcement with id {0}";

    public void validatePermissionToEditAnnouncement(UserInfo userInfo, Announcement announcement) {
        User user = userService.getExistingUser(userInfo.getId());
        if (hasAnyOfRoles(user, UserRoleName.ROLE_ADMIN, UserRoleName.ROLE_MODERATOR)) {
            return;
        }

        if (hasAnyOfRoles(user, UserRoleName.ROLE_USER) && isAnnouncementCreator(user, announcement)) {
            return;
        }

        throw new IllegalArgumentException(MessageFormat.format(PERMISSION_DENIED_TO_EDIT_ANNOUNCEMENT, String.valueOf(announcement.getId())));

    }

    private boolean hasAnyOfRoles(User user, UserRoleName ...userRoles) {
        Collection<UserRoleName> grantedRoles = user.getRoles().stream()
                .map(UserRole::getName)
                .collect(Collectors.toList());
        Collection<UserRoleName> desiredRoles = Arrays.asList(userRoles);
        return !Collections.disjoint(grantedRoles, desiredRoles);
    }

    private boolean isAnnouncementCreator(User user, Announcement announcement) {
        return announcement.getCreatedBy().getId().equals(user.getId());
    }

}
