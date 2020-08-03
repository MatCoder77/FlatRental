package com.flatrental.domain.announcement;

import com.flatrental.api.AnnouncementBrowseDTO;
import com.flatrental.api.AnnouncementDTO;
import com.flatrental.api.AnnouncementSearchResultDTO;
import com.flatrental.api.ResourceDTO;
import com.flatrental.api.ResponseDTO;
import com.flatrental.domain.announcement.search.SearchCriteria;
import com.flatrental.domain.announcement.search.SearchCriteriaService;
import com.flatrental.domain.managedobject.ManagedObjectState;
import com.flatrental.domain.user.User;
import com.flatrental.domain.user.UserService;
import com.flatrental.infrastructure.security.HasAnyRole;
import com.flatrental.infrastructure.security.HasModeratorOrAdminRole;
import com.flatrental.infrastructure.security.LoggedUser;
import com.flatrental.infrastructure.security.UserInfo;
import com.flatrental.infrastructure.utils.Exceptions;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import static com.flatrental.infrastructure.utils.ResourcePaths.ID;
import static com.flatrental.infrastructure.utils.ResourcePaths.ID_PATH;

@Api(tags = "Announcements")
@RestController
@RequestMapping("/api/announcements")
@RequiredArgsConstructor
public class AnnouncementController {

    private final AnnouncementService announcementService;
    private final AnnouncementMapper announcementMapper;
    private final UserService userService;
    private final SearchCriteriaService searchCriteriaService;

    private static final String STATE = "state";
    private static final String CHANGE_STATE_RESOURCE = "/change-state";

    @GetMapping(ID_PATH)
    public AnnouncementDTO getAnnouncement(@PathVariable(ID) Long id, @LoggedUser UserInfo userInfo) {
        Announcement announcement = announcementService.getExistingAnnouncementAndIncrementViewsCounter(id);
        Optional<User> user = userService.findUser(userInfo);
        return announcementMapper.mapToAnnouncementDTO(announcement, user);
    }

    @PostMapping
    @HasAnyRole
    public ResourceDTO createNewAnnouncement(@Valid @RequestBody AnnouncementDTO announcementDTO) {
        Announcement announcementToCreate = announcementMapper.mapToAnnouncement(announcementDTO);
        Announcement createdAnnouncement = announcementService.createAnnouncement(announcementToCreate);
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
        Announcement announcementToUpdate = announcementMapper.mapToAnnouncement(updatedAnnouncementDTO);
        Announcement updatedAnnouncement = announcementService.updateAnnouncement(id, announcementToUpdate, userInfo);
        return generateResourceDTO(updatedAnnouncement);
    }

    @DeleteMapping(ID_PATH)
    @HasModeratorOrAdminRole
    public ResponseDTO deleteAnnouncement(@PathVariable(ID) Long id, @LoggedUser UserInfo userInfo) {
        announcementService.removeAnnouncement(id, userInfo);
        return ResponseDTO.builder()
                .success(true)
                .build();
    }

    @GetMapping("/search")
    public AnnouncementSearchResultDTO searchAnnouncements(@RequestParam("searchCriteria") Optional<String> searchCriteriaParam, Pageable pageable, @LoggedUser UserInfo userInfo) {
        SearchCriteria searchCriteria = searchCriteriaParam.map(searchCriteriaService::getSearchCriteria)
                .orElseGet(SearchCriteria::new);
        Page<Announcement> announcementPage = announcementService.searchAnnouncements(searchCriteria, pageable);
        Optional<User> user = userService.findUser(userInfo);
        return announcementMapper.mapToAnnouncementSearchResultDTO(announcementPage, searchCriteria, user);
    }

    @GetMapping("/permissions" + ID_PATH)
    public ResponseDTO checkPermissionToModifyAnnouncement(@PathVariable(ID) Long id, @LoggedUser UserInfo userInfo) {
        boolean hasPermission = announcementService.hasPermissionToEditAnnouncement(id, userInfo);
        return ResponseDTO.builder()
                .success(hasPermission)
                .build();
    }

    @PostMapping("/add-to-favourites" + ID_PATH)
    @Transactional
    @HasAnyRole
    public ResponseDTO addAnnouncementsToFavourites(@PathVariable(ID) Long announcementId, @LoggedUser UserInfo userInfo) {
        User user = userService.findUser(userInfo)
                .orElseThrow(Exceptions::getCannotGetUserFromContextException);
        announcementService.addAnnouncementsToFavourites(announcementId, user);
        return ResponseDTO.builder()
                .success(true)
                .message("added")
                .build();
    }

    @DeleteMapping("/remove-from-favourites" + ID_PATH)
    @Transactional
    @HasAnyRole
    public ResponseDTO removeAnnouncementsFromFavourites(@PathVariable(ID) Long announcementId, @LoggedUser UserInfo userInfo) {
        User user = userService.findUser(userInfo)
                .orElseThrow(Exceptions::getCannotGetUserFromContextException);
        announcementService.removeAnnouncementsFromFavourites(announcementId, user);
        return ResponseDTO.builder()
                .success(true)
                .message("removed")
                .build();
    }

    @GetMapping("/favourites")
    @HasAnyRole
    public List<AnnouncementDTO> getFavouriteAnnouncements(@LoggedUser UserInfo userInfo) {
        User user = userService.findUser(userInfo)
                .orElseThrow(Exceptions::getCannotGetUserFromContextException);
        return user.getFavourites().stream()
                .map(announcement -> announcementMapper.mapToAnnouncementDTO(announcement, user))
                .collect(Collectors.toList());
    }

    @GetMapping("/user" + ID_PATH)
    @HasAnyRole
    public List<AnnouncementBrowseDTO> getUserAnnouncements(Pageable pageable, @LoggedUser UserInfo userInfo) {
        User user = userService.findUser(userInfo)
                .orElseThrow(Exceptions::getCannotGetUserFromContextException);
        SearchCriteria searchCriteria = SearchCriteria.builder()
                .author(user.getId())
                .allowedManagedObjectStates(Set.of(ManagedObjectState.ACTIVE, ManagedObjectState.INACTIVE))
                .build();
        Page<Announcement> announcementPage = announcementService.searchAnnouncements(searchCriteria, pageable);
        return announcementMapper.mapToAnnouncementSearchResultDTO(announcementPage, searchCriteria, user).getAnnouncements();
    }

    @PutMapping(ID_PATH + CHANGE_STATE_RESOURCE)
    @HasAnyRole
    public ResponseDTO changeAnnouncementState(@PathVariable(ID) Long announcementId, @RequestParam(STATE) ManagedObjectState objectState, @LoggedUser UserInfo userInfo) {
        announcementService.changeAnnouncementState(announcementId, objectState, userInfo);
        return ResponseDTO.builder()
                .success(true)
                .build();
    }

}
