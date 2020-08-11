package com.flatrental.domain.permissions;

import com.flatrental.domain.announcement.Announcement;
import com.flatrental.domain.managedobject.ManagedObjectState;
import com.flatrental.domain.user.User;
import com.flatrental.domain.user.UserService;
import com.flatrental.domain.user.UserRole;
import com.flatrental.infrastructure.security.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Set;

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
        return hasAnyOfRoles(user, UserRole.ROLE_MODERATOR, UserRole.ROLE_ADMIN) ||
                (hasAnyOfRoles(user, UserRole.ROLE_USER) && isAnnouncementCreator(user, announcement));
    }

    private boolean hasAnyOfRoles(User user, UserRole...allowedRoles) {
        return Set.of(allowedRoles).contains(user.getRole());
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
        return hasAnyOfRoles(user, UserRole.ROLE_MODERATOR, UserRole.ROLE_ADMIN) ||
                (managedObjectState != ManagedObjectState.REMOVED && hasAnyOfRoles(user, UserRole.ROLE_USER) && isAnnouncementCreator(user, announcement));
    }

}
