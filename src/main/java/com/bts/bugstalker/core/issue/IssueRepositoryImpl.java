package com.bts.bugstalker.core.issue;

import com.bts.bugstalker.core.common.repository.BaseRepositoryImpl;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class IssueRepositoryImpl extends BaseRepositoryImpl<IssueEntity, Long> implements IssueRepository {

    private final QIssueEntity issue = QIssueEntity.issueEntity;

    public IssueRepositoryImpl(EntityManager em) {
        super(IssueEntity.class, em);
    }

    @EntityGraph(attributePaths = {"labels", "project", "reporter", "assignee"})
    List<IssueEntity> getAllByProjectId(Long id) {
        return queryFactory
                .select(issue)
                .from(issue)
                .where(issue.project.id.eq(id))
                .fetch();
    }

    IssueEntity getByProjectTagAndId(String tag, Long id) {
        return queryFactory
                .select(issue)
                .from(issue)
                .where(issue.project.tag.eq(tag).and(issue.id.eq(id)))
                .fetchOne();
    }

    Page<IssueEntity> getAllByProjectIdPaged(Long projectId, int page, int pageSize) {
        JPAQuery<IssueEntity> query = queryFactory
                .select(issue)
                .from(issue)
                .where(issue.project.id.eq(projectId));

        long totalElements = query.fetchCount();
        List<IssueEntity> issues = query
                .limit(pageSize)
                .offset((long) page * pageSize)
                .fetch();
        Pageable pageable = Pageable.ofSize(pageSize);
        return new PageImpl<>(issues, pageable, totalElements);
    }
}
