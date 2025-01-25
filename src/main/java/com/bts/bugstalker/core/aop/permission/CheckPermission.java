package com.bts.bugstalker.core.aop.permission;

import com.bts.bugstalker.common.enums.Permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Annotation used on rest api methods to check for business permissions.
 * Uses SecurityContextHolder to retrieve authenticated user and verifies against given permission.
 * Annotated method returns 403 - FORBIDDEN status when no permission.
 * @see CheckPermissionAspect
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckPermission {

    Permission permission();
}
