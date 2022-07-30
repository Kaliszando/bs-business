package com.bts.bugstalker.core.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByUsername(String username);

    @Query("SELECT u FROM UserEntity u " +
            "WHERE UPPER(u.username) LIKE %?1% OR UPPER(u.email) LIKE %?1% " +
            "OR UPPER(CONCAT(u.lastName, u.firstName, u.lastName)) LIKE %?1%")
    List<UserEntity> searchByQuery(String query);

}
