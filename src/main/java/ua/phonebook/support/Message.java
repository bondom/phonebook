package ua.phonebook.support;

import java.util.Locale;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

/**
 * Helper to simplify accessing i18n messages in code.
 * 
 * This class uses hard-coded English locale.
 */
@Component
public class Message {
	
	@Autowired
	private MessageSource messageSource;
	
	private MessageSourceAccessor accessor;

	@PostConstruct
	public void init(){
		accessor = new MessageSourceAccessor(messageSource, Locale.ENGLISH);
	}
	
	public String getMessage(String code){
		return accessor.getMessage(code);
	}

}
