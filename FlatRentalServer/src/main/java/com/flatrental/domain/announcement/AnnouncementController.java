package com.flatrental.domain.announcement;

import com.flatrental.api.AnnouncementDTO;
import com.flatrental.api.ResourceDTO;
import com.flatrental.infrastructure.security.LoggedUser;
import com.flatrental.infrastructure.security.UserInfo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/announcement")
public class AnnouncementController {

    @PostMapping("/create")
    @PreAuthorize("hasRole('USER')")
    public ResourceDTO createNewAnnouncement(@LoggedUser UserInfo userInfo, AnnouncementDTO announcementDTO) {
        return null;
    }
}
