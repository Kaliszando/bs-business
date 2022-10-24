package com.bts.bugstalker.core.issue;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class IssueManager {

    private final IssueService issueService;

    public void createIssue(IssueEntity issue) {
        issueService.save(issue);
    }

}
