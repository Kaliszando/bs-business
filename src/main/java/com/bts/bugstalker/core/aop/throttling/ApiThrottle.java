package com.bts.bugstalker.core.aop.throttling;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used on rest api methods to limit api calls.
 * Limit is the value of how many times method can be called in one minute.
 * Can be configured to preserve limitation cap per user or globally.
 * @see ApiThrottleAspect
 * @see ThrottlingScope
 * @see ThrottlingAlgorithm
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiThrottle {

    ThrottlingAlgorithm algorithm() default ThrottlingAlgorithm.FIXED_WINDOW_COUNTER;

    ThrottlingScope scope() default ThrottlingScope.PER_USER;

    int limit() default 60;

}
