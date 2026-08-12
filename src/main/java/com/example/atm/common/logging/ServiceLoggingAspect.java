package com.example.atm.common.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ServiceLoggingAspect {

  @Around("execution(* com.example.atm..service..*(..))")
  public Object logServiceMethod(ProceedingJoinPoint joinPoint) throws Throwable {

    long start = System.currentTimeMillis();

    String methodName = joinPoint.getSignature().toShortString();

    log.info("SERVICE IN | method={}", methodName);

    try {

      Object result = joinPoint.proceed();

      long executionTime = System.currentTimeMillis() - start;

      log.info("SERVICE OUT | method={} time={}ms", methodName, executionTime);

      return result;

    } catch (Exception ex) {

      long executionTime = System.currentTimeMillis() - start;

      log.error("SERVICE ERROR | method={} time={}ms", methodName, executionTime, ex);

      throw ex;
    }
  }
}
