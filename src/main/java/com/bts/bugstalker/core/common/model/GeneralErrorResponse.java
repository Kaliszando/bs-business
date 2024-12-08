package com.bts.bugstalker.core.common.model;

import lombok.Builder;

//TODO move to api specification
@Builder
public record GeneralErrorResponse(String code) {
}
