package com.bts.bugstalker.core.issue.converter;

import com.bts.bugstalker.api.model.IssueDetailsDto;
import com.bts.bugstalker.core.issue.IssueEntity;
import org.mapstruct.Mapper;

@Mapper
public interface IssueMapper {

    IssueDetailsDto mapToDetailDto(IssueEntity issue);

}
