package com.example.checkrepo.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class LoggingAspect {
    @Pointcut("execution(public * com.example.checkrepo.controller.*.*(..))")
    public void controllerLog() {}

    @Pointcut("execution(public * com.example.checkrepo.service.*.*(..))")
    public void serviceLog() {}

   @AfterReturning(returning = "returnObject", pointcut = "controllerLog()")
    public void afterReturning(Object returnObject) {
        log.info("return value: {}", returnObject);
   }

   @After("controllerLog()")
    public void doAfter(JoinPoint joinPoint) {
        log.info("method executed succesfully: {}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
   }

   @Around("controllerLog()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;
        log.info("Execution method: {}.{}. Execution {}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), executionTime);
        return proceed;
   }
}
