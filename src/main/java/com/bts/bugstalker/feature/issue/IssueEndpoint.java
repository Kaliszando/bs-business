package com.bts.bugstalker.feature.issue;

import com.bts.bugstalker.common.enums.Permission;
import com.bts.bugstalker.feature.issue.converter.IssueConverter;
import com.bts.bugstalker.core.aop.permission.CheckPermission;
import com.bts.bugstalker.core.aop.throttling.ApiThrottle;
import com.bts.bugstalker.core.aop.throttling.ThrottlingAlgorithm;
import com.bts.bugstalker.core.aop.throttling.ThrottlingScope;
import com.bts.bugstalker.util.parameters.ApiPaths;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.api.IssueApi;
import org.openapitools.model.*;
import org.springframework.data.domain.Page;
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

    private final IssueService issueService;

    private final IssueConverter converter;

    @Override
    @CheckPermission(permission = Permission.CAN_CREATE_NEW_ISSUE)
    public ResponseEntity<Void> createIssue(@Valid IssueDetailsDto issueDetails) {
        issueService.createIssue(converter.toEntity(issueDetails));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<List<IssueInfoDto>> getAllIssuesByProjectId(@NotNull @Valid Long projectId) {
        List<IssueEntity> issues = issueService.getAllByProjectId(projectId);
        return ResponseEntity.ok(converter.toInfoDtoList(issues));
    }

    @Override
    @ApiThrottle(algorithm = ThrottlingAlgorithm.FIXED_WINDOW_COUNTER, limit = 23, scope = ThrottlingScope.PER_USER)
    public ResponseEntity<IssuePageResponse> getIssuePage(@Valid IssuePageRequest request) {
        Page<IssueEntity> page = issueService.getIssuesPaged(request);
        return ResponseEntity.ok(converter.toPageResponse(page));
    }

    @Override
    public ResponseEntity<IssueDetailsDto> getIssueByTagId(String tagId) {
        IssueEntity issue = issueService.getByTagId(tagId);
        return ResponseEntity.ok(converter.toDetailsDto(issue));
    }

    @Override
    public ResponseEntity<IssueDetailsDto> partialIssueUpdate(@Valid IssuePartialUpdate update) {
        IssueEntity issue = issueService.partialUpdate(update.getTagId(), update.getStatus(), update.getBacklog());
        return ResponseEntity.ok(converter.toDetailsDto(issue));
    }

    @Override
    public ResponseEntity<IssueDetailsDto> updateIssue(@Valid IssueDetailsDto updateRequest) {
        IssueEntity updateModel = converter.toEntity(updateRequest);
        IssueEntity updated = issueService.updateIssue(updateModel);
        return ResponseEntity.ok(converter.toDetailsDto(updated));
    }
}
