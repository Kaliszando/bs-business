package com.bts.bugstalker.feature.issue.converter;

import com.bts.bugstalker.feature.issue.IssueEntity;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.IssueDetailsDto;
import org.openapitools.model.IssueInfoDto;
import org.openapitools.model.IssuePageResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class IssueConverterImpl implements IssueConverter {

    private final IssueInfoConverter issueInfoConverter;

    private final IssueDetailsConverter issueDetailsConverter;

    @Override
    public IssueEntity toEntity(IssueDetailsDto dto) {
        return issueDetailsConverter.convert(dto);
    }

    @Override
    public IssueDetailsDto toDetailsDto(IssueEntity entity) {
        return issueDetailsConverter.convertBack(entity);
    }

    @Override
    public List<IssueInfoDto> toInfoDtoList(List<IssueEntity> issues) {
        return issueInfoConverter.convertBack(issues);
    }

    @Override
    public IssuePageResponse toPageResponse(Page<IssueEntity> page) {
        IssuePageResponse response = new IssuePageResponse();
        response.setIssues(issueInfoConverter.convertBack(page.getContent()));
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        return response;
    }
}
