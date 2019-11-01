package com.flatrental.domain.announcement.search;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class SearchCriteriaService {

    @Autowired
    private ObjectMapper objectMapper;

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

}
