package com.shoppingoo.brand.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@EnableAspectJAutoProxy
public class LoggerAspect {

    @Pointcut("execution(* com.shoppingoo.brand.*.controller..*(..)) || execution(* com.shoppingoo.brand.*.service..*(..)) || execution(* com.shoppingoo.brand.*.mapper..*(..))")
    private void loggerTarget() {
        // AOP target pointcut
    }

    @Around("loggerTarget()")
    public Object loggerPointer(ProceedingJoinPoint joinPoint) throws Throwable {
        String type = "";
        String className = joinPoint.getSignature().getDeclaringTypeName();

        if (className.contains("Controller")) {
            type = "[Controller]";
        } else if (className.contains("ServiceImpl")) {
            type = "[ServiceImpl]";
        } else if (className.contains("Mapper")) {
            type = "[Mapper]";
        }

        String methodName = joinPoint.getSignature().getName();

        log.debug(type + " " + className + "." + methodName);
        return joinPoint.proceed();
    }
}
