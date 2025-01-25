package com.bts.bugstalker.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Permission {

    CAN_CREATE_NEW_ISSUE("CAN_CREATE_NEW_ISSUE");

    private final String name;
}
