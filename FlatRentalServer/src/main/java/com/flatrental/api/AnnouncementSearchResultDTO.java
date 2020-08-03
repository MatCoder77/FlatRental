package com.flatrental.api;

import com.flatrental.domain.announcement.search.SearchCriteria;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URI;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementSearchResultDTO {

    private List<AnnouncementBrowseDTO> announcements;
    private Long totalSize;
    private Long pageSize;
    private Long pageNumber;
    private URI firstPage;
    private URI previousPage;
    private URI nextPage;
    private URI lastPage;
    private String pageWithSelectedNumber;
    private SearchCriteria criteria;

}
