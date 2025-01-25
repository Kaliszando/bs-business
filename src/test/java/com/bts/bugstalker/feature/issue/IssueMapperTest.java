package com.bts.bugstalker.feature.issue;

import com.bts.bugstalker.feature.issue.converter.IssueMapper;
import com.bts.bugstalker.mocks.fixtures.DtoMocks;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

public class IssueMapperTest {

    private final IssueMapper mapper = Mappers.getMapper(IssueMapper.class);

    //TODO add mapper tests
    @Test
    void shouldMapDtoToEntityCorrectly() {
        var dto = DtoMocks.ISSUE_DETAILS.prepareIssueDetailsDto();

        mapper.mapToDetailDto(IssueEntity.builder().build());
    }
}
