package com.flatrental.domain.locations.elasticsearch;

import com.flatrental.infrastructure.security.HasAdminRole;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @PostMapping("/createIndex")
    @HasAdminRole
    public Boolean createIndex() throws IOException {
        locationService.createLocationIndex();
        return true;
    }

    @PostMapping("/reindexation")
    @HasAdminRole
    public Boolean reindexAllLocations() throws IOException {
        locationService.reindexLocations();
        return true;
    }

    @GetMapping("/search")
    public List<LocationSearchDTO> searchLocation(@RequestParam(name = "searchText") String searchText) throws IOException {
        return locationService.searchLocation(searchText);
    }
}
