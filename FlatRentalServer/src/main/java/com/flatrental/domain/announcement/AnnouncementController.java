package com.flatrental.domain.announcement;

import com.flatrental.api.AnnouncementDTO;
import com.flatrental.api.ResourceDTO;
import com.flatrental.api.ResponseDTO;
import com.flatrental.domain.permissions.PermissionsValidationService;
import com.flatrental.infrastructure.security.HasAnyRole;
import com.flatrental.infrastructure.security.LoggedUser;
import com.flatrental.infrastructure.security.UserInfo;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/announcements")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    @Autowired
    private PermissionsValidationService permissionsValidationService;

    private static final String ID = "id";
    private static final String ID_PATH = "/{" + ID + "}";

    @GetMapping(ID_PATH)
    public AnnouncementDTO getAnnouncement(@PathVariable(ID) Long id) {
        Announcement announcement = announcementService.getExistingAnnouncement(id);
        return announcementService.mapToAnnouncementDTO(announcement);
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
        return new ResourceDTO(uri);
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
    @HasAnyRole
    public ResponseDTO deleteAnnouncement(@PathVariable(ID) Long id, @LoggedUser UserInfo userInfo) {
        Announcement announcement = announcementService.getExistingAnnouncement(id);
        permissionsValidationService.validatePermissionToEditAnnouncement(userInfo, announcement);
        boolean isSuccessfullyRemoved = announcementService.removeAnnouncement(announcement);
        return ResponseDTO.builder()
                .success(isSuccessfullyRemoved)
                .build();
    }

}
