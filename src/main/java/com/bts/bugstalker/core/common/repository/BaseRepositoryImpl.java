package com.bts.bugstalker.core.common.repository;

import com.bts.bugstalker.core.common.exception.MandatoryEntityNotFoundException;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.openapitools.model.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import java.util.List;

public abstract class BaseRepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID> implements BaseRepository<T, ID> {

    private final EntityManager em;

    protected final JPAQueryFactory queryFactory;

    public BaseRepositoryImpl(Class<T> domainClass, EntityManager em) {
        super(domainClass, em);
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public T findByIdMandatory(ID id) throws MandatoryEntityNotFoundException {
        return findById(id).orElseThrow(
                () -> new MandatoryEntityNotFoundException(getDomainClass().getSimpleName(), id.toString()));
    }

    @Override
    public void clear() {
        em.clear();
    }

    @Override
    public void detach(T entity) {
        em.detach(entity);
    }

    protected String addWildcards(String prompt) {
        return "%".concat(prompt).concat("%");
    }

    protected Page<T> executePaging(PageRequest request, JPAQuery<T> query) {
        long totalElements = query.fetchCount();
        List<T> issues = query
                .limit(request.getPageSize())
                .offset((long) request.getPage() * request.getPageSize())
                .fetch();
        Pageable pageable = Pageable.ofSize(request.getPageSize());
        return new PageImpl<>(issues, pageable, totalElements);
    }
}