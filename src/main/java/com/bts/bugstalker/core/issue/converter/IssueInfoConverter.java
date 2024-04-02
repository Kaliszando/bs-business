package com.bts.bugstalker.core.issue.converter;

import com.bts.bugstalker.api.model.IssueInfoDto;
import com.bts.bugstalker.core.common.generic.BidirectionalConverter;
import com.bts.bugstalker.core.issue.IssueEntity;
import com.bts.bugstalker.core.user.UserMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class IssueInfoConverter implements BidirectionalConverter<IssueInfoDto, IssueEntity> {

    private final UserMapper userMapper;

    @Override
    public IssueEntity convert(IssueInfoDto dto) {
        throw new NotImplementedException();
    }

    @Override
    public IssueInfoDto convertBack(IssueEntity entity) {
        var dto = new IssueInfoDto();
        dto.setId(entity.getId());
        dto.setIssueType(IssueInfoDto.IssueTypeEnum.valueOf(entity.getType().getCode()));
        dto.setIssueSeverity(IssueInfoDto.IssueSeverityEnum.valueOf(entity.getSeverity().getCode()));
        dto.projectId(entity.getProject().getId());
        dto.tagId(entity.getProject().getTag().concat("-").concat(entity.getId().toString()));
        dto.status(entity.getStatus());
        dto.name(entity.getName());
        dto.summary(entity.getSummary());
        dto.labels(entity.getLabels());
        dto.epicName(entity.getEpic() != null ? entity.getEpic().getName() : null);
        dto.reporter(userMapper.mapToDto(entity.getReporter()));
        dto.assignee(userMapper.mapToDto(entity.getAssignee()));
        dto.backlogList(entity.getBacklogList());
        return dto;
    }
}
