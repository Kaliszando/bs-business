package com.bts.bugstalker.feature.aop.permission;

import com.bts.bugstalker.core.shared.enums.Permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckPermission {

    Permission permission();
}
