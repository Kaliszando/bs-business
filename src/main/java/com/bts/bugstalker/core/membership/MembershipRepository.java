package com.bts.bugstalker.core.membership;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MembershipRepository extends JpaRepository<MembershipEntity, Long> {

    List<MembershipEntity> findAllByUserId(Long userId);

    @Query("SELECT m.id FROM MembershipEntity m WHERE m.user.id = ?1")
    List<Long> findAllIdsByUserId(Long userId);

    @Query("SELECT m.project.id FROM MembershipEntity m WHERE m.user.id = ?1")
    List<Long> findAllProjectIdsByUserId(Long userId);
}
