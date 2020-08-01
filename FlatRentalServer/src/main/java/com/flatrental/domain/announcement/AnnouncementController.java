package com.flatrental.domain.announcement;

import com.flatrental.api.AnnouncementDTO;
import com.flatrental.api.AnnouncementSearchResultDTO;
import com.flatrental.api.ResourceDTO;
import com.flatrental.api.ResponseDTO;
import com.flatrental.domain.announcement.search.SearchCriteria;
import com.flatrental.domain.announcement.search.SearchCriteriaService;
import com.flatrental.domain.managedobject.ManagedObjectState;
import com.flatrental.domain.permissions.PermissionsValidationService;
import com.flatrental.domain.user.User;
import com.flatrental.domain.user.UserService;
import com.flatrental.infrastructure.security.HasAnyRole;
import com.flatrental.infrastructure.security.HasModeratorOrAdminRole;
import com.flatrental.infrastructure.security.LoggedUser;
import com.flatrental.infrastructure.security.UserInfo;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Api(tags = "Announcements")
@RestController
@RequestMapping("/api/announcements")
@RequiredArgsConstructor
public class AnnouncementController {

    private final AnnouncementService announcementService;
    private final PermissionsValidationService permissionsValidationService;
    private final UserService userService;
    private final SearchCriteriaService searchCriteriaService;

    private static final String ID = "id";
    private static final String ID_PATH = "/{" + ID + "}";
    private static final String STATE = "state";
    private static final String CHANGE_STATE_RESOURCE = "/change-state";

    @GetMapping(ID_PATH)
    public AnnouncementDTO getAnnouncement(@PathVariable(ID) Long id, @LoggedUser UserInfo userInfo) {
        Announcement announcement = announcementService.getExistingAnnouncement(id);
        Optional<User> user = Optional.ofNullable(userInfo).map(UserInfo::getId)
                .map(userService::getExistingUser);
        announcementService.incrementViewsCounter(announcement, user);
        return announcementService.mapToAnnouncementDTO(announcement, user);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResourceDTO createNewAnnouncement(@Valid @RequestBody AnnouncementDTO announcementDTO) {
        Announcement createdAnnouncement = announcementService.createAnnouncement(announcementDTO);
        return generateResourceDTO(createdAnnouncement);
    }

    private ResourceDTO generateResourceDTO(Announcement announcement) {
        String announcementId = String.valueOf(announcement.getId());
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/announcements/{id}")
                .buildAndExpand(announcementId)
                .toUri();
        return ResourceDTO.builder()
                .id(announcement.getId())
                .uri(uri)
                .build();
    }

    @PutMapping(ID_PATH)
    @HasAnyRole
    public ResourceDTO updateAnnouncement(@PathVariable(ID) Long id, @Valid @RequestBody AnnouncementDTO updatedAnnouncementDTO, @LoggedUser UserInfo userInfo) {
        Announcement announcement = announcementService.getExistingAnnouncement(id);
        permissionsValidationService.validatePermissionToEditAnnouncement(userInfo, announcement);
        Announcement updatedAnnouncement = announcementService.updateAnnouncement(announcement, updatedAnnouncementDTO);
        return generateResourceDTO(updatedAnnouncement);
    }

    @DeleteMapping(ID_PATH)
    @HasModeratorOrAdminRole
    public ResponseDTO deleteAnnouncement(@PathVariable(ID) Long id, @LoggedUser UserInfo userInfo) {
        Announcement announcement = announcementService.getExistingAnnouncement(id);
        permissionsValidationService.validatePermissionToEditAnnouncement(userInfo, announcement);
        boolean isSuccessfullyRemoved = announcementService.removeAnnouncement(announcement);
        return ResponseDTO.builder()
                .success(isSuccessfullyRemoved)
                .build();
    }

    @GetMapping("/search")
    public AnnouncementSearchResultDTO searchAnnouncements(@RequestParam("searchCriteria") Optional<String> searchCriteriaParam, Pageable pageable, @LoggedUser UserInfo userInfo) {
        Optional<User> user = Optional.ofNullable(userInfo).map(info -> userService.getExistingUser(info.getId()));
        SearchCriteria searchCriteria = searchCriteriaParam.map(searchCriteriaService::getSearchCriteria)
                .orElseGet(SearchCriteria::new);
        return announcementService.searchAnnouncements(searchCriteria, pageable, user);
    }

    @GetMapping("/permissions" + ID_PATH)
    public ResponseDTO checkPermissionToModifyAnnouncement(@PathVariable(ID) Long id, @LoggedUser UserInfo userInfo) {
        Announcement announcement = announcementService.getExistingAnnouncement(id);
        boolean hasPermission = permissionsValidationService.hasPermissionToEditAnnouncement(userInfo, announcement);
        return ResponseDTO.builder()
                .success(hasPermission)
                .build();
    }

    @PostMapping("/add-to-favourites" + ID_PATH)
    @Transactional
    @HasAnyRole
    public ResponseDTO addAnnouncementsToFavourites(@PathVariable(ID) Long announcementId, @LoggedUser UserInfo userInfo) {
        Announcement announcement = announcementService.getExistingAnnouncement(announcementId);
        User user = userService.getExistingUser(userInfo.getId());
        announcementService.addAnnouncementsToFavourites(announcement, user);
        return ResponseDTO.builder()
                .success(true)
                .message("addedt")
                .build();
    }

    @DeleteMapping("/remove-from-favourites" + ID_PATH)
    @Transactional
    @HasAnyRole
    public ResponseDTO removeAnnouncementsFromFavourites(@PathVariable(ID) Long announcementId, @LoggedUser UserInfo userInfo) {
        Announcement announcement = announcementService.getExistingAnnouncement(announcementId);
        User user = userService.getExistingUser(userInfo.getId());
        announcementService.removeAnnouncementsFromFavourites(announcement, user);
        return ResponseDTO.builder()
                .success(true)
                .message("removed")
                .build();
    }

    @GetMapping("/favourites")
    @HasAnyRole
    public List<AnnouncementDTO> getFavouriteAnnouncements(@LoggedUser UserInfo userInfo) {
        User user = userService.getExistingUser(userInfo.getId());
        Set<Announcement> announcements = user.getFavourites();
        return announcements.stream()
                .map(announcement -> announcementService.mapToAnnouncementDTO(announcement, user))
                .collect(Collectors.toList());
    }

    @GetMapping("/user" + ID_PATH)
    @HasAnyRole
    public List<AnnouncementDTO> getUserAnnouncements(Pageable pageable, @LoggedUser UserInfo userInfo) {
        Optional<User> user = Optional.ofNullable(userInfo).map(info -> userService.getExistingUser(info.getId()));
        SearchCriteria searchCriteria = SearchCriteria.builder()
                .author(userInfo.getId())
                .allowedManagedObjectStates(Set.of(ManagedObjectState.ACTIVE, ManagedObjectState.INACTIVE))
                .build();
        return announcementService.searchAnnouncements(searchCriteria, pageable, user).getAnnouncements();
    }

    @PutMapping(ID_PATH + CHANGE_STATE_RESOURCE)
    @HasAnyRole
    public ResponseDTO changeAnnouncementState(@PathVariable(ID) Long announcementId, @RequestParam(STATE) ManagedObjectState objectState, @LoggedUser UserInfo userInfo) {
        Announcement announcement = announcementService.getExistingAnnouncement(announcementId);
        permissionsValidationService.validatePermissionsToChangeAnnouncementState(userInfo, objectState, announcement);
        announcementService.changeAnnouncementState(announcement, objectState);
        return ResponseDTO.builder()
                .success(true)
                .build();
    }


}
