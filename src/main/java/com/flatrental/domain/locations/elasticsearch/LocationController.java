package com.flatrental.domain.locations.elasticsearch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/locations")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @PostMapping("/createIndex")
    @PreAuthorize("hasRole('ADMIN')")
    public Boolean createIndex() throws IOException {
        locationService.createLocationIndex();
        return true;
    }

    @PostMapping("/reindexation")
    @PreAuthorize("hasRole('ADMIN')")
    public Boolean reindexAllLocations() throws IOException {
        locationService.indexLocations();
        return true;
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('USER')")
    public List<LocationSearchDTO> searchLocation(@RequestParam(name = "searchText") String searchText) throws IOException {
        return locationService.searchLocation(searchText);
    }
}
