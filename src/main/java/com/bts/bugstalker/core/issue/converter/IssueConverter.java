package com.bts.bugstalker.core.issue.converter;

import com.bts.bugstalker.api.model.IssueDetailsDto;
import com.bts.bugstalker.api.model.IssueInfoDto;
import com.bts.bugstalker.api.model.IssuePageResponse;
import com.bts.bugstalker.core.issue.IssueEntity;
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
