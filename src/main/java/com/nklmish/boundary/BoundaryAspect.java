package com.nklmish.boundary;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

@Aspect
public class BoundaryAspect {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Pointcut("within(com.nklmish.boundary..*Controller)")
    public void pointCut() {}

    @AfterThrowing(pointcut = "pointCut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        log.error("Encountered exception - {}.{}() details = {}", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), e.getCause(), e);
    }

    @Around("pointCut()")
    public Object logAround(ProceedingJoinPoint point) throws Throwable {
        Signature signature = point.getSignature();

        log.info("Entering: {}.{}() params - {}", signature.getDeclaringTypeName(),
                    signature.getName(), Arrays.toString(point.getArgs()));
        try {
            Object result = point.proceed();
                log.info("Leaving: {}.{}() result = {}", signature.getDeclaringTypeName(),
                        signature.getName(), result);
            return result;
        } catch (Exception ex) {
            log.error("Encountered exception: {} in {}.{}()", Arrays.toString(point.getArgs()),
                    signature.getDeclaringTypeName(), signature.getName());

            throw ex;
        }
    }

}
