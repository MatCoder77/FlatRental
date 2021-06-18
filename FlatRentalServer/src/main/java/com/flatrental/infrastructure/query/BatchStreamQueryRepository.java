package com.flatrental.infrastructure.query;

import com.flatrental.infrastructure.stream.StreamUtils;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true)
public class BatchStreamQueryRepository {

    @PersistenceContext
    private EntityManager em;

    public <T> Stream<List<T>> getBatchedResultStream(Class<T> entityClass, int batchSize) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        Root<T> root = criteriaQuery.from(entityClass);
        criteriaQuery.select(root);
        return getBatchedResultStream(criteriaQuery, batchSize);
    }

    public <T> Stream<List<T>> getBatchedResultStream(CriteriaQuery criteriaQuery, int batchSize) {
        StatelessSession session = em.unwrap(Session.class).getSessionFactory().openStatelessSession();
            Stream queryStream = session.createQuery(criteriaQuery)
                    .setFetchSize(Integer.MIN_VALUE) // to enable Streaming in MySQL JDBC Driver
                    .stream();
            Stream<List<T>> batchedStream = StreamUtils.batchedStream(queryStream, batchSize);
            batchedStream.onClose(() -> closeQueryStreamAndSession(queryStream, session));
        return batchedStream;
    }

    private void closeQueryStreamAndSession(Stream stream, StatelessSession session) {
        stream.close();
        session.close();
    }

}
