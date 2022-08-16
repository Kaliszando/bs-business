package com.bts.bugstalker.core.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum IssueSeverity {

    BLOCKER("BLOCKER", "blocks development and/or testing work, production could not run"),
    CRITICAL("CRITICAL", "crashes, loss of data, severe memory leak"),
    MAJOR("MAJOR", "major loss of function"),
    NORMAL("NORMAL", "default severity, selected for tasks other than bugs"),
    MINOR("MINOR", "minor loss of function or other problem where easy workaround is present"),
    TRIVIAL("TRIVIAL", "cosmetic problem like misspelled words or misaligned text");

    private final String code;

    private final String description;

}
