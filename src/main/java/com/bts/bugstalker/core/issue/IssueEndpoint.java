package com.bts.bugstalker.core.issue;

import com.bts.bugstalker.api.IssueApi;
import com.bts.bugstalker.api.model.IssueDetailsDto;
import com.bts.bugstalker.api.model.IssueInfoDto;
import com.bts.bugstalker.util.parameters.ApiPaths;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiPaths.V1)
public class IssueEndpoint implements IssueApi {

    private final IssueDetailsConverter issueDetailsConverter;

    private final IssueInfoConverter issueInfoConverter;

    private final IssueManager issueManager;

    private final IssueService issueService;

    @Override
    public ResponseEntity<Void> createIssue(@Valid IssueDetailsDto issueDetails) {
        issueManager.createIssue(issueDetailsConverter.convert(issueDetails));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<List<IssueInfoDto>> getAllIssuesByProjectId(@NotNull @Valid Long projectId) {
        List<IssueEntity> issues = issueService.getAllByProjectId(projectId);
        return ResponseEntity.ok(issueInfoConverter.reverseConvert(issues));
    }

    @Override
    public ResponseEntity<IssueDetailsDto> getIssueByTagId(String tagId) {
        IssueEntity issue = issueService.getByTagId(tagId);
        return ResponseEntity.ok(issueDetailsConverter.reverseConvert(issue));
    }
}
