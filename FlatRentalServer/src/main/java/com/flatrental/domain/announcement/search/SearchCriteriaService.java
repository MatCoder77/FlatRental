package com.flatrental.domain.announcement.search;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flatrental.domain.managedobject.ManagedObjectState;
import com.flatrental.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SearchCriteriaService {

    private final ObjectMapper objectMapper;

    private static final String INVALID_CRITERIA = "Supplied invalid criteria";

    public SearchCriteria getSearchCriteria(String urlEncodedSearch) {
        String base64SearchCriteria = URLDecoder.decode(urlEncodedSearch, StandardCharsets.UTF_8);
        byte[] decodedBytes = Base64.getDecoder().decode(base64SearchCriteria);
        String searchCriteriaJSON = new String(decodedBytes);
        try {
            return objectMapper.readValue(searchCriteriaJSON, SearchCriteria.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(INVALID_CRITERIA);
        }
    }

    public SearchCriteria getSearchCriteriaForAnnouncementNotRemovedAndCreatedByUser(User user) {
        return SearchCriteria.builder()
                .author(user.getId())
                .allowedManagedObjectStates(Set.of(ManagedObjectState.ACTIVE, ManagedObjectState.INACTIVE))
                .build();
    }

}
