package com.example.checkrepo.aspect;

import com.example.checkrepo.exception.IncorrectInputException;
import com.example.checkrepo.exception.ObjectNotFoundException;
import com.example.checkrepo.exception.TypeMissMatchException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Arrays;

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
    public void logError(Exception exception) {
        long start = System.currentTimeMillis();
        long executionTime = System.currentTimeMillis() - start;
        if (exception instanceof ObjectNotFoundException || exception instanceof MethodArgumentTypeMismatchException) {
            log.error("Exception thrown {}, {}", exception, executionTime);
        }
    }

    @Around("controllerLog()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        log.info("Executed successfully: {}.{}.", joinPoint.getSignature()
                .getDeclaringTypeName(), joinPoint.getSignature().getName());
        return proceed;
    }
}
