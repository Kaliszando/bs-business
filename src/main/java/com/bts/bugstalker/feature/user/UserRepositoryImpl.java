package com.bts.bugstalker.feature.user;

import com.bts.bugstalker.common.repository.BaseRepositoryImpl;
import com.bts.bugstalker.feature.membership.QMembershipEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl extends BaseRepositoryImpl<UserEntity, Long> implements UserRepository {

    final QUserEntity user = QUserEntity.userEntity;

    final QMembershipEntity membership = QMembershipEntity.membershipEntity;

    UserRepositoryImpl(EntityManager em) {
        super(UserEntity.class, em);
    }

    Optional<UserEntity> findByEmail(String email) {
        return Optional.ofNullable(queryFactory
                .select(user)
                .from(user)
                .where(user.email.equalsIgnoreCase(email))
                .fetchFirst()
        );
    }

    public Optional<UserEntity> findByUsername(String username) {
        return Optional.ofNullable(queryFactory
                .select(user)
                .from(user)
                .where(user.username.equalsIgnoreCase(username))
                .fetchFirst()
        );
    }

    List<UserEntity> searchByQuery(String query, Long projectId) {
        String sqlQuery = query != null ? addWildcards(query) : "%";
        return queryFactory
                .select(user)
                .from(user)
                .innerJoin(membership)
                .on(membership.user.id.eq(user.id))
                .where(membership.project.id.eq(projectId))
                .where(user.email.likeIgnoreCase(sqlQuery)
                        .or(user.username.likeIgnoreCase(sqlQuery)
                        .or(user.lastName.likeIgnoreCase(sqlQuery)
                        .or(user.firstName.likeIgnoreCase(sqlQuery)
                        .or(user.lastName.concat(user.firstName).concat(user.lastName).likeIgnoreCase(sqlQuery))
                        ))))
                .fetch();
    }

    List<UserEntity> findByProjectId(Long projectId) {
        return queryFactory
                .select(user)
                .from(user)
                .innerJoin(membership)
                .on(membership.user.id.eq(user.id))
                .where(membership.project.id.eq(projectId))
                .fetch();
    }
}
