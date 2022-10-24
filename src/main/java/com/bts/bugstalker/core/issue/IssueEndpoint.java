package com.bts.bugstalker.core.issue;

import com.bts.bugstalker.api.IssueApi;
import com.bts.bugstalker.api.model.IssueDetailsDto;
import com.bts.bugstalker.api.model.IssueInfoDto;
import com.bts.bugstalker.api.model.IssuePartialUpdate;
import com.bts.bugstalker.core.shared.enums.Permission;
import com.bts.bugstalker.feature.aop.permission.CheckPermission;
import com.bts.bugstalker.util.parameters.ApiPaths;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(ApiPaths.V1)
public class IssueEndpoint implements IssueApi {

    private final IssueDetailsConverter issueDetailsConverter;

    private final IssueInfoConverter issueInfoConverter;

    private final IssueManager issueManager;

    private final IssueService issueService;

    @Override
    @CheckPermission(permission = Permission.CAN_CREATE_NEW_ISSUE)
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

    @Override
    public ResponseEntity<IssueDetailsDto> partialIssueUpdate(@Valid IssuePartialUpdate update) {
        IssueEntity issue = issueService.partialUpdate(update.getTagId(), update.getStatus(), update.getBacklog());
        return ResponseEntity.ok(issueDetailsConverter.reverseConvert(issue));
    }

    @Override
    public ResponseEntity<IssueDetailsDto> updateIssue(@Valid IssueDetailsDto updateRequest) {
        IssueEntity updateModel = issueDetailsConverter.convert(updateRequest);
        IssueEntity updated = issueService.updateIssue(updateModel);
        return ResponseEntity.ok(issueDetailsConverter.reverseConvert(updated));
    }
}
