package com.bts.bugstalker.core.issue;

import com.bts.bugstalker.core.common.repository.BaseRepositoryImpl;
import com.querydsl.core.types.OrderSpecifier;
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

    Page<IssueEntity> getAllByProjectIdPaged(Long projectId, int page, int pageSize, String orderBy) {
        JPAQuery<IssueEntity> query = queryFactory
                .select(issue)
                .from(issue)
                .where(issue.project.id.eq(projectId));

        long totalElements = query.fetchCount();
        List<IssueEntity> issues = query
                .orderBy(extractSortBy(orderBy))
                .limit(pageSize)
                .offset((long) page * pageSize)
                .fetch();
        Pageable pageable = Pageable.ofSize(pageSize);
        return new PageImpl<>(issues, pageable, totalElements);
    }

    private OrderSpecifier<?> extractSortBy(String orderBy) {
        if (orderBy == null) {
            return issue.modifiedDate.asc();
        }
        return switch (orderBy) {
            case "id" -> issue.id.asc();
            case "-id" -> issue.id.desc();
            case "username" -> issue.assignee.username.lower().asc();
            case "-username" -> issue.assignee.username.lower().desc();
            case "status" -> issue.status.lower().asc();
            case "-status" -> issue.status.lower().desc();
            case "title" -> issue.name.lower().asc();
            case "-title" -> issue.name.lower().desc();
            case "epic" -> issue.epic.name.lower().asc();
            case "-epic" -> issue.epic.name.lower().desc();
            default -> issue.modifiedDate.asc();
        };
    }
}
