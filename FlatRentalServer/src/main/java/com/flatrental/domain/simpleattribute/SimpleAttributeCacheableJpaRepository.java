package com.flatrental.domain.simpleattribute;

import com.flatrental.infrastructure.utils.StringConstants;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.QueryHint;
import java.util.List;

@NoRepositoryBean
public interface SimpleAttributeCacheableJpaRepository<T, ID> extends JpaRepository<T, ID> {

    String SIMPLE_ATTR_QUERY_CACHE = "simpleAttributeQueryCache";

    @Override
    @QueryHints({
            @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = StringConstants.TRUE),
            @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHE_REGION, value = SIMPLE_ATTR_QUERY_CACHE)
    })
    List<T> findAll();

    @Override
    @QueryHints({
            @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = StringConstants.TRUE),
            @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHE_REGION, value = SIMPLE_ATTR_QUERY_CACHE)
    })
    List<T> findAll(Sort sort);

    @Override
    @QueryHints({
            @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = StringConstants.TRUE),
            @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHE_REGION, value = SIMPLE_ATTR_QUERY_CACHE)
    })
    List<T> findAllById(Iterable<ID> iterable);

    @Override
    @QueryHints({
            @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = StringConstants.TRUE),
            @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHE_REGION, value = SIMPLE_ATTR_QUERY_CACHE)
    })
    <S extends T> List<S> findAll(Example<S> example);

    @Override
    @QueryHints({
            @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = StringConstants.TRUE),
            @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHE_REGION, value = SIMPLE_ATTR_QUERY_CACHE)
    })
    <S extends T> List<S> findAll(Example<S> example, Sort sort);

    @Override
    @QueryHints({
            @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = StringConstants.TRUE),
            @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHE_REGION, value = SIMPLE_ATTR_QUERY_CACHE)
    })
    <S extends T> Page<S> findAll(Example<S> example, Pageable pageable);
}
