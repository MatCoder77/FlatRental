package com.flatrental.domain.announcement;

import com.flatrental.domain.announcement.search.SearchCriteria;
import org.springframework.data.domain.Page;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.awt.print.Pageable;

public class CustomAnnouncementRepositoryImpl implements CustomAnnouncementRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Announcement> searchAnnouncementsByCriteria(SearchCriteria searchCriteria, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Announcement> criteriaQuery = criteriaBuilder.createQuery(Announcement.class);
        Root<Announcement> a = criteriaQuery.from(Announcement.class);
        criteriaQuery.select(a);
        return null;

    }

    private Predicate getIntegerRangePredicate() {
        return null;
    }

}
