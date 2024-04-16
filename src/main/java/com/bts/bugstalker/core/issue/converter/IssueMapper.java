package com.bts.bugstalker.core.issue.converter;

import com.bts.bugstalker.core.issue.IssueEntity;
import org.mapstruct.Mapper;
import org.openapitools.model.IssueDetailsDto;

@Mapper
public interface IssueMapper {

    IssueDetailsDto mapToDetailDto(IssueEntity issue);

}
