package com.bts.bugstalker.core.issue;

import com.bts.bugstalker.core.issue.converter.IssueMapper;
import com.bts.bugstalker.fixtures.DtoMocks;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

public class IssueMapperTest {

    private final IssueMapper mapper = Mappers.getMapper(IssueMapper.class);

    @Test
    void shouldMapDtoToEntityCorrectly() {
        var dto = DtoMocks.ISSUE_DETAILS.prepareIssueDetailsDto();

        mapper.mapToDetailDto(IssueEntity.builder().build());

    }
}
