package app.springproject.aspect;

import java.time.Duration;
import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class MethodCallLoggingAspect {
  @Before("execution( * app.springproject.controller.*.*(..))")
  public void logControllerMethod(JoinPoint joinPoint) {
    log.info("Метод {} был вызван", joinPoint.getSignature().getName());
  }

  @Around("execution( * app.springproject.controller.*.*(..))")
  public void logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
    Instant timeBeforeExecution = Instant.now();
    joinPoint.proceed();
    Instant timeAfterExecution = Instant.now();
    Duration duration = Duration.between(timeBeforeExecution, timeAfterExecution);
    log.info("Время выполнения {}: {} ms", joinPoint.getSignature().getName(), duration.toMillis());
  }
}
