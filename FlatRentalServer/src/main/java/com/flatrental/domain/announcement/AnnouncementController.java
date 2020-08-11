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

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static com.flatrental.infrastructure.utils.ResourcePaths.ID;
import static com.flatrental.infrastructure.utils.ResourcePaths.ID_PATH;

@Api(tags = "Announcements")
@RestController
@RequestMapping(AnnouncementController.MAIN_RESOURCE)
@Transactional
@RequiredArgsConstructor
public class AnnouncementController {

    public static final String MAIN_RESOURCE = "/api/announcements";
    private static final String STATE = "state";
    private static final String CHANGE_STATE_RESOURCE = "/change-state";
    public static final String SEARCH_RESOURCE = "/search";

    private final AnnouncementService announcementService;
    private final AnnouncementMapper announcementMapper;
    private final UserService userService;
    private final SearchCriteriaService searchCriteriaService;

    @GetMapping(ID_PATH)
    public AnnouncementDTO getAnnouncement(@PathVariable(ID) Long id) {
        Announcement announcement = announcementService.getExistingAnnouncementAndIncrementViewsCounter(id);
        return announcementMapper.mapToAnnouncementDTO(announcement);
    }

    @PostMapping
    @HasAnyRole
    public ResourceDTO createNewAnnouncement(@Valid @RequestBody AnnouncementDTO announcementDTO) {
        Announcement announcementToCreate = announcementMapper.mapToAnnouncement(announcementDTO);
        Announcement createdAnnouncement = announcementService.createAnnouncement(announcementToCreate);
        return announcementMapper.mapToResourceDTO(createdAnnouncement);
    }

    @PutMapping(ID_PATH)
    @HasAnyRole
    public ResourceDTO updateAnnouncement(@PathVariable(ID) Long id, @Valid @RequestBody AnnouncementDTO updatedAnnouncementDTO, @LoggedUser UserInfo userInfo) {
        Announcement announcementToUpdate = announcementMapper.mapToAnnouncement(updatedAnnouncementDTO);
        Announcement updatedAnnouncement = announcementService.updateAnnouncement(id, announcementToUpdate, userInfo);
        return announcementMapper.mapToResourceDTO(updatedAnnouncement);
    }

    @DeleteMapping(ID_PATH)
    @HasModeratorOrAdminRole
    public ResponseDTO deleteAnnouncement(@PathVariable(ID) Long id, @LoggedUser UserInfo userInfo) {
        announcementService.removeAnnouncement(id, userInfo);
        return ResponseDTO.builder()
                .success(true)
                .build();
    }

    @GetMapping(SEARCH_RESOURCE)
    public AnnouncementSearchResultDTO searchAnnouncements(@RequestParam("searchCriteria") Optional<String> searchCriteriaParam, Pageable pageable, @LoggedUser UserInfo userInfo) {
        SearchCriteria searchCriteria = searchCriteriaParam.map(searchCriteriaService::getSearchCriteria)
                .orElseGet(SearchCriteria::new);
        Page<Announcement> announcementPage = announcementService.searchAnnouncements(searchCriteria, pageable);
        return announcementMapper.mapToAnnouncementSearchResultDTO(announcementPage, searchCriteria);
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
        User user = userService.getExistingUser(userInfo);
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
        User user = userService.getExistingUser(userInfo);
        announcementService.removeAnnouncementsFromFavourites(announcementId, user);
        return ResponseDTO.builder()
                .success(true)
                .message("removed")
                .build();
    }

    @GetMapping("/favourites")
    @HasAnyRole
    public List<AnnouncementDTO> getFavouriteAnnouncements(@LoggedUser UserInfo userInfo) {
        User user = userService.getExistingUser(userInfo);
        return announcementMapper.mapToAnnouncementDTOs(user.getFavourites());
    }

    @GetMapping("/user" + ID_PATH)
    @HasAnyRole
    public List<AnnouncementBrowseDTO> getUserAnnouncements(Pageable pageable, @LoggedUser UserInfo userInfo) {
        User user = userService.getExistingUser(userInfo);
        SearchCriteria searchCriteria = searchCriteriaService.getSearchCriteriaForAnnouncementNotRemovedAndCreatedByUser(user);
        Page<Announcement> announcementPage = announcementService.searchAnnouncements(searchCriteria, pageable);
        return announcementMapper.mapToAnnouncementSearchResultDTO(announcementPage, searchCriteria).getAnnouncements();
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
