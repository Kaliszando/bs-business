package com.bts.bugstalker.common.exception.base;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "of")
public final class ExceptionCode {

    private String value;

}
