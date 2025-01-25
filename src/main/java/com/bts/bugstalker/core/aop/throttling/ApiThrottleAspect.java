package com.bts.bugstalker.core.aop.throttling;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@RequiredArgsConstructor
@Aspect
@Component
public class ApiThrottleAspect {

    private final ThrottlingService throttlingService;

    @Around("@annotation(com.bts.bugstalker.core.aop.throttling.ApiThrottle)")
    public Object throttleApiCall(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        ApiThrottle annotation = method.getAnnotation(ApiThrottle.class);

        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();

        throttlingService.incrementApiCall(className, methodName, annotation);

        return joinPoint.proceed();
    }
}
