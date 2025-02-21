package com.bts.bugstalker.feature.issue;

import com.bts.bugstalker.common.enums.IssueSeverity;
import com.bts.bugstalker.common.enums.IssueType;
import com.bts.bugstalker.common.model.BasePageData;
import com.bts.bugstalker.common.repository.BaseRepositoryImpl;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.lang3.StringUtils;
import org.openapitools.model.IssuePageFilter;
import org.openapitools.model.IssuePageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
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

    public IssueEntity getByTitle(String title) {
        return queryFactory
                .select(issue)
                .from(issue)
                .where(issue.name.eq(title))
                .fetchOne();
    }

    Page<IssueEntity> getAllByProjectIdPaged(IssuePageRequest request) {
        JPAQuery<IssueEntity> jpaQuery = prepareBasePageQuery(request);
        addFilter(request.getFilter(), jpaQuery.getMetadata());
        BasePageData pageData = BasePageData.builder()
                .page(request.getPage())
                .pageSize(request.getPageSize())
                .sortBy(request.getSortBy())
                .build();
        return executePaging(pageData, jpaQuery);
    }

    private JPAQuery<IssueEntity> prepareBasePageQuery(IssuePageRequest request) {
        return queryFactory
                .select(issue)
                .from(issue)
                .where(issue.project.id.eq(request.getProjectId()))
                .orderBy(extractSortBy(request.getSortBy()));
    }

    private void addFilter(IssuePageFilter filter, QueryMetadata metadata) {
        if (filter == null) {
            return;
        }

        if (StringUtils.isNotBlank(filter.getQuery())) {
            metadata.addWhere(issue.name.likeIgnoreCase(addWildcards(filter.getQuery()))
                    .or(issue.project.tag.concat("-").concat(issue.id.stringValue())
                            .likeIgnoreCase(addWildcards(filter.getQuery()))));
        }
        if (filter.getTypes() != null && !filter.getTypes().isEmpty()) {
            metadata.addWhere(issue.type.in(filter.getTypes().stream()
                    .map(type -> IssueType.valueOf(type.getValue()))
                    .toList()));
        }
        if (filter.getSeverities() != null && !filter.getSeverities().isEmpty()) {
            metadata.addWhere(issue.severity.in(filter.getSeverities().stream()
                    .map(severity -> IssueSeverity.valueOf(severity.getValue()))
                    .toList()));
        }
        if (filter.getStatuses() != null && !filter.getStatuses().isEmpty()) {
            metadata.addWhere(issue.status.in(filter.getStatuses()));
        }
        if (filter.getReporterId() != null) {
            metadata.addWhere(issue.reporter.id.eq(filter.getReporterId()));
        }
        if (filter.getAssigneeId() != null) {
            metadata.addWhere(issue.assignee.id.eq(filter.getAssigneeId()));
        }
        if (filter.getStartDate() != null || filter.getEndDate() != null) {
            metadata.addWhere(issue.createdDate.between(
                    LocalDate.parse(filter.getStartDate()).atStartOfDay(),
                    LocalDate.parse(filter.getEndDate()).atStartOfDay().plusDays(1))
            );
        }
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
