package com.bts.bugstalker.core.issue;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IssueManager {

    private final IssueService issueService;

    public void createIssue(IssueEntity issue) {
        issueService.save(issue);
    }
}
