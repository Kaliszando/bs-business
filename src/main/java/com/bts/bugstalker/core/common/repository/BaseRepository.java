package com.bts.bugstalker.core.common.repository;

import com.bts.bugstalker.core.common.exception.MandatoryEntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepository<T, ID> extends JpaRepository<T, ID> {

    T findByIdMandatory(ID id) throws MandatoryEntityNotFoundException;

    void clear();

    void detach(T entity);
}
