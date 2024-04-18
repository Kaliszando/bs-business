package com.bts.bugstalker.core.common.mapper;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BasePageData {
    private Integer page;
    private Integer pageSize;
    private String sortBy;
}
