package com.bts.bugstalker.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum IssueType {

    EPIC("EPIC"),
    TASK("TASK"),
    SUBTASK("SUBTASK"),
    ENHANCEMENT("ENHANCEMENT"),
    BUG("BUG");

    private final String code;
}
