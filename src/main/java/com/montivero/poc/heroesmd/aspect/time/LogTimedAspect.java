package com.montivero.poc.heroesmd.aspect.time;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j(topic = "LogTimed")
public class LogTimedAspect {

   @Pointcut("@annotation(com.montivero.poc.heroesmd.aspect.time.LogTimed)")
   private void hasLogTimedAnnotation() {
   }

   @Around("hasLogTimedAnnotation()")
   public Object executionTimed(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

      long startTime = System.currentTimeMillis();
      long endTime = 0;
      boolean wasSuccessfully = false;
      Object object;
      Signature signature = proceedingJoinPoint.getSignature();
      try {
         object = proceedingJoinPoint.proceed();
         endTime = System.currentTimeMillis();
         wasSuccessfully = true;
      } catch (Throwable tw) {
         endTime = System.currentTimeMillis();
         throw tw;
      } finally {
         long finalTime = endTime - startTime;
         if (wasSuccessfully) {
            log.info("Time from {} was: {}ms", signature.getName(), finalTime);
         } else {
            log.info("Time from {} with exception was: {}ms", signature.getName(), finalTime);
         }
      }

      return object;
   }

}
