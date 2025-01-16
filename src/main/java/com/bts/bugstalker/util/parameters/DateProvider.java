package com.bts.bugstalker.util.parameters;

import lombok.experimental.UtilityClass;

import java.util.Date;

@UtilityClass
public class DateProvider {

    public static Date currentTimePlusMinutes(Long minutes) {
        return new Date(System.currentTimeMillis() + minutes * 60 * 1000);
    }
}
