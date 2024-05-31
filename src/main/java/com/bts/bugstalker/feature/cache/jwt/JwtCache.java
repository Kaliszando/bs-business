package com.bts.bugstalker.feature.cache.jwt;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JwtCache extends CrudRepository<JwtEntity, Long> {

    boolean existsByToken(String token);
}
