package com.bts.bugstalker.core.common.repository;

import com.bts.bugstalker.core.common.exception.MandatoryEntityNotFoundException;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;

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
}