package com.bts.bugstalker.core.issue;

import com.bts.bugstalker.api.model.IssueDetailsDto;
import com.bts.bugstalker.api.model.IssueInfoDto;
import com.bts.bugstalker.core.common.enums.IssueSeverity;
import com.bts.bugstalker.core.common.enums.IssueType;
import com.bts.bugstalker.core.project.ProjectService;
import com.bts.bugstalker.core.user.UserMapper;
import com.bts.bugstalker.feature.context.ContextProvider;
import com.bts.bugstalker.util.generic.TwoWayConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.threeten.bp.OffsetDateTime;

@RequiredArgsConstructor
@Component
public class IssueDetailsConverter implements TwoWayConverter<IssueDetailsDto, IssueEntity> {

    private final ProjectService projectService;

    private final ContextProvider contextProvider;

    private final UserMapper userMapper;

    @Override
    public IssueEntity convert(IssueDetailsDto dto) {
        return IssueEntity.builder()
                .project(projectService.getById(dto.getProjectId()))
                .type(IssueType.valueOf(dto.getIssueType().toString()))
                .severity(IssueSeverity.valueOf(dto.getIssueSeverity().toString()))
                .status(getDefaultStatus(dto.getStatus()))
                .name(dto.getName())
                .summary(dto.getSummary())
                .description(dto.getDescription())
                .labels(dto.getLabels())
                .reporter(contextProvider.getUser())
                .backlogList(getDefaultBacklogList(dto.getLocation()))
                .build();
    }

    @Override
    public IssueDetailsDto reverseConvert(IssueEntity entity) {
        IssueDetailsDto issue = new IssueDetailsDto()
                .id(entity.getId())
                .issueType(IssueDetailsDto.IssueTypeEnum.fromValue(entity.getType().getCode()))
                .issueSeverity(IssueDetailsDto.IssueSeverityEnum.valueOf(entity.getSeverity().getCode()))
                .projectId(entity.getProject().getId())
                .tagId(entity.getProject().getTag().concat("-").concat(entity.getId().toString()))
                .status(entity.getStatus())
                .name(entity.getName())
                .summary(entity.getSummary())
                .labels(entity.getLabels())
                .epicName(entity.getEpic() != null ? entity.getEpic().getName() : null)
                .reporter(userMapper.mapToDto(entity.getReporter()))
                .assignee(userMapper.mapToDto(entity.getAssignee()))
                .description(entity.getDescription())

                .daysOld(3)
                .hoursSpent(12);

        issue.createdOn(OffsetDateTime.now().minusDays(3));
        issue.modifiedOn(OffsetDateTime.now());

        return issue;
    }

    private String getDefaultStatus(String status) {
        return status == null || status.isBlank() ? "to do" : status;
    }

    private String getDefaultBacklogList(String listName) {
        return listName == null || listName.isBlank() ? "inactive" : listName;
    }
}
