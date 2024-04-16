package com.bts.bugstalker.core.issue.converter;

import com.bts.bugstalker.core.issue.IssueEntity;
import org.openapitools.model.IssueDetailsDto;
import org.openapitools.model.IssueInfoDto;
import org.openapitools.model.IssuePageResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IssueConverter {

    // InfoDto

    List<IssueInfoDto> toInfoDtoList(List<IssueEntity> issues);

    // DetailsDto

    IssueEntity toEntity(IssueDetailsDto dto);

    IssueDetailsDto toDetailsDto(IssueEntity entity);

    // Other

    IssuePageResponse toPageResponse(Page<IssueEntity> page);
}
