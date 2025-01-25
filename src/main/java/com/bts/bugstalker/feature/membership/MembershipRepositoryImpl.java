package com.bts.bugstalker.feature.membership;

import com.bts.bugstalker.common.repository.BaseRepositoryImpl;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class MembershipRepositoryImpl extends BaseRepositoryImpl<MembershipEntity, Long> implements MembershipRepository {

    private final QMembershipEntity membership = QMembershipEntity.membershipEntity;

    public MembershipRepositoryImpl(EntityManager em) {
        super(MembershipEntity.class, em);
    }

    public List<MembershipEntity> findAllByUserId(Long userId) {
        return queryFactory
                .select(membership)
                .from(membership)
                .where(membership.user.id.eq(userId))
                .fetch();
    }

    public List<Long> findAllIdsByUserId(Long userId) {
        return queryFactory
                .select(membership.id)
                .from(membership)
                .where(membership.user.id.eq(userId))
                .fetch();
    }

    public List<Long> findAllProjectIdsByUserId(Long userId) {
        return queryFactory
                .select(membership.project.id)
                .from(membership)
                .where(membership.user.id.eq(userId))
                .fetch();
    }
}
