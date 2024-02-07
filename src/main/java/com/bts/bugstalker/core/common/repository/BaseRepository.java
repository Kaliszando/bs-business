package com.bts.bugstalker.core.common.repository;

import com.bts.bugstalker.core.common.exception.CommonExceptions;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

@NoRepositoryBean
public interface BaseRepository<T, ID> extends PagingAndSortingRepository<T, ID> {

    T findByIdMandatory(ID id) throws CommonExceptions.DbQueryResultNotFound;

    void clear();

    void detach(T entity);
}
