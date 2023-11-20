package edu.jcourse.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CommonPointcuts {
    @Pointcut("@within(org.springframework.stereotype.Controller) || @within(org.springframework.web.bind.annotation.RestController)")
    public void isControllerLayer() {
    }

    @Pointcut("within(edu.jcourse.service.*Service)")
    public void isServiceLayer() {
    }

    @Pointcut("@within(org.springframework.web.bind.annotation.ControllerAdvice)")
    public void isControllerAdvice() {
    }
}