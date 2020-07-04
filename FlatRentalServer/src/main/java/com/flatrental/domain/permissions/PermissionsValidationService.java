package com.flatrental.domain.permissions;

import com.flatrental.domain.announcement.Announcement;
import com.flatrental.domain.managedobject.ManagedObjectState;
import com.flatrental.domain.user.User;
import com.flatrental.domain.user.UserService;
import com.flatrental.domain.userrole.UserRole;
import com.flatrental.domain.userrole.UserRoleName;
import com.flatrental.infrastructure.security.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionsValidationService {

    private final UserService userService;

    private static final String PERMISSION_DENIED_TO_EDIT_ANNOUNCEMENT = "You have no permission to edit announcement with id {0}";
    private static final String NO_PERMISSION_TO_CHANGE_ANNOUNCEMENT_STATE = "You have no permission to change announcement state to {0}";

    public void validatePermissionToEditAnnouncement(UserInfo userInfo, Announcement announcement) {
        if (!hasPermissionToEditAnnouncement(userInfo, announcement)) {
            throw new IllegalArgumentException(MessageFormat.format(PERMISSION_DENIED_TO_EDIT_ANNOUNCEMENT, String.valueOf(announcement.getId())));

        }
    }

    public boolean hasPermissionToEditAnnouncement(UserInfo userInfo, Announcement announcement) {
        if (userInfo == null) {
            return false;
        }

        User user = userService.getExistingUser(userInfo.getId());
        if (hasAnyOfRoles(user, UserRoleName.ROLE_MODERATOR, UserRoleName.ROLE_ADMIN)) {
            return true;
        }

        if (hasAnyOfRoles(user, UserRoleName.ROLE_USER) && isAnnouncementCreator(user, announcement)) {
            return true;
        }

        return false;
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

    public void validatePermissionsToChangeAnnouncementState(UserInfo userInfo, ManagedObjectState managedObjectState, Announcement announcement) {
        if (!hasPermissionToChangeAnnouncementState(userInfo, managedObjectState, announcement)) {
            throw new IllegalArgumentException(MessageFormat.format(NO_PERMISSION_TO_CHANGE_ANNOUNCEMENT_STATE, managedObjectState));
        }
    }

    private boolean hasPermissionToChangeAnnouncementState(UserInfo userInfo, ManagedObjectState managedObjectState, Announcement announcement) {
        if (userInfo == null) {
            return false;
        }
        User user = userService.getExistingUser(userInfo.getId());
        if (hasAnyOfRoles(user, UserRoleName.ROLE_MODERATOR, UserRoleName.ROLE_ADMIN)) {
            return true;
        }
        if (managedObjectState != ManagedObjectState.REMOVED && hasAnyOfRoles(user, UserRoleName.ROLE_USER) && isAnnouncementCreator(user, announcement)) {
            return true;
        }
        return false;
    }

}
