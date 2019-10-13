package com.flatrental.domain.announcement;

import com.flatrental.domain.announcement.search.SearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomAnnouncementRepository {

    Page<Announcement> searchAnnouncementsByCriteria(SearchCriteria searchCriteria, Pageable pageable);

}
