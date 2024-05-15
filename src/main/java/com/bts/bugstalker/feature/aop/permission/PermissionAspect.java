package com.bts.bugstalker.feature.aop.permission;

import com.bts.bugstalker.core.permission.PermissionManager;
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
public class PermissionAspect {

    private final PermissionManager permissionManager;

    @Around("@annotation(CheckPermission)")
    public Object checkPermissionByUserContext(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        CheckPermission annotation = method.getAnnotation(CheckPermission.class);

        if (permissionManager.contextUserHasPermission(annotation.permission())) {
            return joinPoint.proceed();
        }
        log.warn("User has no permission ".concat(annotation.permission().getName()));
        return ResponseEntity.unprocessableEntity().build();
    }
}
