package com.urs.booking.utility;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggingAspect {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);
	
	@AfterThrowing(pointcut = "execution(* com.urs.booking.api.*.*(..))", throwing = "exception")
	public void logAfterThrowingAdviceDetails(JoinPoint joinPoint, Exception exception) {
		LOGGER.info("In After throwing Advice for Controller method, Joinpoint signature :{}", joinPoint.getSignature());
		LOGGER.error(exception.getMessage());
		System.out.println();
	}
	
	@AfterThrowing(pointcut = "execution(* com.urs.booking.service.*.*(..))", throwing = "exception")
	public void logAfterThrowingAdviceDetailsSer(JoinPoint joinPoint, Exception exception) {
		LOGGER.info("In After throwing Advice for Service method, Joinpoint signature :{}", joinPoint.getSignature());
		LOGGER.error(exception.getMessage());
		System.out.println();
	}
}
