package com.eaa.login.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class LoggingAspect {
	
	@Pointcut(value = "execution(* com.eaa.login.appuser.*.*(..) )")
	public void appUser() {

	}

	@Pointcut(value = "execution(* com.eaa.login.registration.*.*(..) )")
	public void Registration() {

	}
	
	@Around("appUser() || Registration() ")
	public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
		
		Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass().getName());
		
		logger.info(joinPoint.getSignature().getName() + " - ENTER");
		
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Object retVal = joinPoint.proceed();
		stopWatch.stop();
		
		logger.info(joinPoint.getSignature().getName() + " - EXIT, took "+ stopWatch.getTotalTimeMillis() + " milliseconds");
		
		return retVal; 
	}

	 

}
