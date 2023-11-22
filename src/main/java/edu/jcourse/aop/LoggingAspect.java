package edu.jcourse.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    private static final String EXCEPTION_MESSAGE = "invoke method {} in class {}, with exception {}: {}";

    @Before(value = "edu.jcourse.aop.CommonPointcuts.isServiceLayer()")
    public void logBefore(JoinPoint joinPoint) {
        log.info("invoke method {} in class {}, with parameters {}",
                joinPoint.getSignature().getName(), joinPoint.getTarget().getClass(), joinPoint.getArgs());
    }

    @AfterThrowing(value = "edu.jcourse.aop.CommonPointcuts.isServiceLayer()", throwing = "exception")
    public void logAfterThrowingInService(JoinPoint joinPoint, Exception exception) {
        log.error(EXCEPTION_MESSAGE,
                joinPoint.getSignature().getName(), joinPoint.getTarget().getClass(), exception.getClass(), exception.getMessage());
    }

    @AfterThrowing(value = "edu.jcourse.aop.CommonPointcuts.isControllerLayer()", throwing = "exception")
    public void logAfterThrowingInController(JoinPoint joinPoint, Exception exception) {
        log.error(EXCEPTION_MESSAGE,
                joinPoint.getSignature().getName(), joinPoint.getTarget().getClass(), exception.getClass(), exception.getMessage());
    }

    @AfterReturning(value = "edu.jcourse.aop.CommonPointcuts.isServiceLayer()", returning = "result")
    public void logAfterReturningInService(JoinPoint joinPoint, Object result) {
        log.info("invoke method {} in class {}, with result {}",
                joinPoint.getSignature().getName(), joinPoint.getTarget().getClass(), result);
    }

    @Before(value = "edu.jcourse.aop.CommonPointcuts.isControllerAdvice() && args(*, java.lang.Exception)")
    public void logBeforeControllerAdvice(JoinPoint joinPoint) {
        Exception exception = (Exception) joinPoint.getArgs()[1];
        log.debug(EXCEPTION_MESSAGE,
                joinPoint.getSignature().getName(), joinPoint.getTarget().getClass(), exception.getClass(), exception.getMessage());
    }
}