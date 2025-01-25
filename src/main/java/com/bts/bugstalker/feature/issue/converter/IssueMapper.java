package com.bts.bugstalker.feature.issue.converter;

import com.bts.bugstalker.feature.issue.IssueEntity;
import org.mapstruct.Mapper;
import org.openapitools.model.IssueDetailsDto;

@Mapper
public interface IssueMapper {

    IssueDetailsDto mapToDetailDto(IssueEntity issue);

}
