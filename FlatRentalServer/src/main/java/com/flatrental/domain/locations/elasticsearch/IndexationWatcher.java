package com.flatrental.domain.locations.elasticsearch;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.concurrent.ConcurrentHashMap;

@Service
@ApplicationScope
public class IndexationWatcher {

    private final ConcurrentHashMap<String, IndexationStatus> statusByIndexationIdentifier = new ConcurrentHashMap<>();

    public IndexationStatus getIndexationStatus(String indexationIdentifier) {
        return statusByIndexationIdentifier.get(indexationIdentifier);
    }

    public void setIndexationStatus(IndexationStatus status, String indexationIdentifier) {
        statusByIndexationIdentifier.put(indexationIdentifier, status);
    }

}
