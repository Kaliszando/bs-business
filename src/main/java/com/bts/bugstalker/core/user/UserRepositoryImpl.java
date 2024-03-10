package com.bts.bugstalker.core.user;

import com.bts.bugstalker.core.common.repository.BaseRepositoryImpl;
import com.bts.bugstalker.core.membership.QMembershipEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl extends BaseRepositoryImpl<UserEntity, Long> implements UserRepository {

    private final QUserEntity user = QUserEntity.userEntity;

    private final QMembershipEntity membership = QMembershipEntity.membershipEntity;

    public UserRepositoryImpl(EntityManager em) {
        super(UserEntity.class, em);
    }

    public Optional<UserEntity> findByEmail(String email) {
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

    public List<UserEntity> searchByQuery(String query) {
        String sqlQuery = addWildcards(query);
        return queryFactory
                .select(user)
                .from(user)
                .where(user.email.likeIgnoreCase(sqlQuery)
                        .or(user.username.likeIgnoreCase(sqlQuery)
                        .or(user.lastName.likeIgnoreCase(sqlQuery)
                        .or(user.firstName.likeIgnoreCase(sqlQuery)
                        .or(user.lastName.concat(user.firstName).concat(user.lastName).likeIgnoreCase(sqlQuery))
                        ))))
                .fetch();
    }

    public List<UserEntity> findByProjectId(Long projectId) {
        return queryFactory
                .select(user)
                .from(user)
                .innerJoin(membership)
                .on(membership.user.id.eq(user.id))
                .where(membership.project.id.eq(projectId))
                .fetch();
    }
}
