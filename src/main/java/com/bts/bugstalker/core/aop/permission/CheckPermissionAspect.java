package com.bts.bugstalker.core.aop.permission;

import com.bts.bugstalker.feature.permission.PermissionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class CheckPermissionAspect {

    private final PermissionManager permissionManager;

    @Around("@annotation(com.bts.bugstalker.core.aop.permission.CheckPermission)")
    public Object checkPermissionByUserContext(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        CheckPermission annotation = method.getAnnotation(CheckPermission.class);

        if (permissionManager.contextUserHasPermission(annotation.permission())) {
            return joinPoint.proceed();
        }
        LOGGER.warn("User has no permission ".concat(annotation.permission().getName()));
        return ResponseEntity.status(403).build();
    }
}
