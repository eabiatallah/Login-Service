package com.eaa.login.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
	
	@Pointcut(value = "execution(* com.eaa.login.appuser.*.*(..) )")
	public void appUser() {

	}

	@Pointcut(value = "execution(* com.eaa.login.registration.*.*(..) )")
	public void Registration() {

	}

	 

}
