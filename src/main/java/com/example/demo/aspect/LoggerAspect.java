package com.example.demo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

public class LoggerAspect {
    @Pointcut("@annotation(com.example.demo.aspect.annotation.LogMe)")
    public void logMeAnnotation(){

    }
    @Before(" logMeAnnotation()")
    public void logBefore(JoinPoint joinPoint){
        System.out.println("Log before the method: " + joinPoint.getSignature().getName());
    }
}
