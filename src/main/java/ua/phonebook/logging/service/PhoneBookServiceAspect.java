package ua.phonebook.logging.service;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import ua.phonebook.model.PhoneBookRecord;
import ua.phonebook.web.viewbean.FilterPhoneBookRecords;


@Aspect
@Component
public class PhoneBookServiceAspect {
	
	Logger logger = LoggerFactory.getLogger(PhoneBookServiceAspect.class);
	
	@SuppressWarnings("unchecked")
	@Around("ua.phonebook.logging.SystemArchitecture.inServiceLayer() &&"
			 + " execution(public * getFilteredPhoneBookByUserLogin(..)) && args(login,filter,pageable)")
	public Page<PhoneBookRecord> filteringOfPhoneBook(ProceedingJoinPoint thisJoinPoint,
										String login,FilterPhoneBookRecords filter,
										Pageable pageable) throws Throwable {
		String className = thisJoinPoint.getTarget().getClass().getName();
		String methodName = thisJoinPoint.getSignature().getName();
		Page<PhoneBookRecord> pageInfo= null;
		try {
			pageInfo = (Page<PhoneBookRecord>)thisJoinPoint.proceed();
		} catch (Throwable e) {
			logger.info("{}.{}(login={},FilterPhoneBookRecords=[{}],Pageable=[{}]):{}:{}",
					className,methodName,login,filter.toString(),pageable.toString(),
					e.getClass(),e.getMessage());
			throw e;
		}
		
		logger.info("{}.{}(login={},FilterPhoneBookRecords=[{}],Pageable=[{}]): "
				+ "{}",
				className,methodName,login,filter.toString(),pageable.toString(),
				pageInfo.toString());

		return pageInfo;
	}
	
	@Around("ua.phonebook.logging.SystemArchitecture.inServiceLayer() &&"
			 + " execution(public * getPhoneBookByUserLogin(..)) && args(login,pageable)")
	public Page<PhoneBookRecord> getPhoneBookByUserLogin(ProceedingJoinPoint thisJoinPoint,
										String login,
										Pageable pageable) throws Throwable {
		String className = thisJoinPoint.getTarget().getClass().getName();
		String methodName = thisJoinPoint.getSignature().getName();
		Page<PhoneBookRecord> pageInfo= null;
		try {
			pageInfo = (Page<PhoneBookRecord>)thisJoinPoint.proceed();
		} catch (Throwable e) {
			logger.info("{}.{}(login={},Pageable=[{}]):{}:{}",
					className,methodName,login,pageable.toString(),
					e.getClass(),e.getMessage());
			throw e;
		}
		
		logger.info("{}.{}(login={},Pageable=[{}]): "
				+ "{}",
				className,methodName,login,pageable.toString(),
				pageInfo.toString());

		return pageInfo;
	}
}
