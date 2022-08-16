package com.bts.bugstalker.core.issue;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
