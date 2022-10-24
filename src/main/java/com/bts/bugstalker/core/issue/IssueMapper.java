package com.bts.bugstalker.core.issue;

import com.bts.bugstalker.api.model.IssueDetailsDto;
import org.mapstruct.Mapper;

@Mapper
public interface IssueMapper {

    IssueDetailsDto mapToDetailDto(IssueEntity issue);

}
