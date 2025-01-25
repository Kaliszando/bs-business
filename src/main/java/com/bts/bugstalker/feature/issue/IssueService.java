package com.bts.bugstalker.feature.issue;

import com.bts.bugstalker.feature.issue.exception.IssueNotFoundException;
import com.bts.bugstalker.feature.issue.exception.IssueOptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.IssuePageRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class IssueService {

    private final IssueRepositoryImpl repository;

    public IssueEntity createIssue(IssueEntity issue) {
        return repository.save(issue);
    }

    public List<IssueEntity> getAllByProjectId(Long projectId) {
        return repository.getAllByProjectId(projectId);
    }

    public Page<IssueEntity> getIssuesPaged(IssuePageRequest request) {
        return repository.getAllByProjectIdPaged(request);
    }

    public IssueEntity getByTagId(String tagId) {
        String[] values = tagId.split("-");
        IssueEntity issue = repository.getByProjectTagAndId(values[0], Long.valueOf(values[1]));
        if (issue == null) {
            throw new IssueNotFoundException(tagId);
        }
        return issue;
    }

    public IssueEntity updateStatus(String tagId, String newStatus) {
        IssueEntity issue = getByTagId(tagId);
        issue.setStatus(newStatus);
        return repository.save(issue);
    }

    public IssueEntity updateIssue(IssueEntity issue) {
        IssueEntity origin = repository.getByProjectTagAndId(issue.getProject().getTag(), issue.getId());

        if (!Objects.equals(origin.getVersion(), issue.getVersion())) {
            throw new IssueOptimisticLockException(issue.getId());
        }

        origin.setName(issue.getName());
        origin.setType(issue.getType());
        origin.setDescription(issue.getDescription());
        origin.setSummary(issue.getSummary());
        origin.setLabels(issue.getLabels());
        origin.setStatus(issue.getStatus());
        origin.setSeverity(issue.getSeverity());
        origin.setAssignee(issue.getAssignee());
        return createIssue(origin);
    }

    public IssueEntity partialUpdate(String tagId, String newStatus, String newBacklogList) {
        if (newStatus != null) {
            return updateStatus(tagId, newStatus);
        }
        return updateBacklogList(tagId, newBacklogList);
    }

    private IssueEntity updateBacklogList(String tagId, String newBacklogList) {
        IssueEntity issue = getByTagId(tagId);
        issue.setBacklogList(newBacklogList);
        return repository.save(issue);
    }
}
