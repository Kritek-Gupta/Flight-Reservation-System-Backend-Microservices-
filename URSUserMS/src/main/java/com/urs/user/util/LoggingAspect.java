package com.urs.user.util;

import java.text.DateFormat;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggingAspect {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);
	
	@Before("execution(* com.urs.user.api.*.*(..))")
	public void logBeforeAdvice(JoinPoint joinPoint) {
		LOGGER.info("In Before Advice, Joinpoint signature :{}", joinPoint.getSignature());
		long time = System.currentTimeMillis();
		String date = DateFormat.getDateTimeInstance().format(time);
		LOGGER.info("Controller Method started at time:{}", date);
		System.out.println();
	}
	
	@AfterReturning("execution(* com.urs.user.api.*.*(..))")
	public void logAfterReturningAdvice(JoinPoint joinPoint) {
		LOGGER.info("In After Returning Advice, Joinpoint signature :{}", joinPoint.getSignature());
		long time = System.currentTimeMillis();
		String date = DateFormat.getDateTimeInstance().format(time);
		LOGGER.info("Controller Method ended at time:{}", date);
		System.out.println();
	}
	
	@AfterThrowing(pointcut = "execution(* com.urs.user.api.*.*(..))", throwing = "exception")
	public void logAfterThrowingAdviceDetails(JoinPoint joinPoint, Exception exception) {
		LOGGER.info("In After throwing Advice for Controller method, Joinpoint signature :{}", joinPoint.getSignature());
		LOGGER.error(exception.getMessage());
		System.out.println();
	}
	
	@Before("execution(* com.urs.user.service.*.*(..))")
	public void logBeforeAdviceSer(JoinPoint joinPoint) {
		LOGGER.info("In Before Advice, Joinpoint signature :{}", joinPoint.getSignature());
		long time = System.currentTimeMillis();
		String date = DateFormat.getDateTimeInstance().format(time);
		LOGGER.info("Service Method started at time:{}", date);
		System.out.println();
	}
	
	@AfterReturning("execution(* com.urs.user.service.*.*(..))")
	public void logAfterReturningAdviceSer(JoinPoint joinPoint) {
		LOGGER.info("In After Returning Advice, Joinpoint signature :{}", joinPoint.getSignature());
		long time = System.currentTimeMillis();
		String date = DateFormat.getDateTimeInstance().format(time);
		LOGGER.info("Service Method ended at time:{}", date);
		System.out.println();
	}
	
	@AfterThrowing(pointcut = "execution(* com.urs.user.service.*.*(..))", throwing = "exception")
	public void logAfterThrowingAdviceDetailsSer(JoinPoint joinPoint, Exception exception) {
		LOGGER.info("In After throwing Advice for Service method, Joinpoint signature :{}", joinPoint.getSignature());
		LOGGER.error(exception.getMessage());
		System.out.println();
	}
}
