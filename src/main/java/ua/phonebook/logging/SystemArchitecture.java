package ua.phonebook.logging;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class SystemArchitecture {
	
	@Pointcut("within(ua.phonebook.service..*)")
	public void inServiceLayer(){}
	
}
