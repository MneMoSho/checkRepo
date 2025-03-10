package com.example.checkrepo.aspect;

import com.example.checkrepo.exception.ObjectNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class LoggingAspect {
    @Pointcut("execution(public * com.example.checkrepo.controller.*.*(..))")
    public void controllerLog() {
    }

    @Pointcut("execution(public * com.example.checkrepo.service.*.*(..))")
    public void serviceLog() {
    }

    @AfterReturning(returning = "returnObject", pointcut = "controllerLog()")
    public void afterReturning(Object returnObject) {
        log.info("return value: {}", returnObject);
    }

    @AfterThrowing(pointcut = "controllerLog()", throwing = "exception")
    public void logError(ObjectNotFoundException exception) {
        long start = System.currentTimeMillis();
        long executionTime = System.currentTimeMillis() - start;
        log.error("Exception thrown {}, {}", exception, executionTime);
    }

    @Around("controllerLog()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;
        log.info("Executed successfully: {}.{}. Execution {}", joinPoint.getSignature()
                .getDeclaringTypeName(), joinPoint.getSignature().getName(), executionTime);
        return proceed;
    }
}
