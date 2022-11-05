package com.bts.bugstalker.core.issue;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class IssueService {

    private final IssueRepository repository;

    public IssueEntity save(IssueEntity issue) {
        return repository.save(issue);
    }

    public List<IssueEntity> getAllByProjectId(Long projectId) {
        return repository.getAllByProjectId(projectId);
    }

    public IssueEntity getByTagId(String tagId) {
        String[] values = tagId.split("-");
        return repository.getByProjectTagAndId(values[0], Long.valueOf(values[1]));
    }

    public IssueEntity updateStatus(String tagId, String newStatus) {
        IssueEntity issue = getByTagId(tagId);
        issue.setStatus(newStatus);
        return repository.save(issue);
    }

    public IssueEntity updateIssue(IssueEntity issue) {
        IssueEntity origin = repository.getByProjectTagAndId(issue.getProject().getTag(), issue.getId());

        if (!Objects.equals(origin.getVersion(), issue.getVersion())) {
            throw IssueExceptionFactory.issueUpdateOptimisticLockException();
        }

        origin.setName(issue.getName());
        origin.setType(issue.getType());
        origin.setDescription(issue.getDescription());
        origin.setSummary(issue.getSummary());
        origin.setLabels(issue.getLabels());
        origin.setStatus(issue.getStatus());
        origin.setSeverity(issue.getSeverity());
        origin.setAssignee(issue.getAssignee());
        return save(origin);
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