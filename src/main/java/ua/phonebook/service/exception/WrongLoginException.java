package ua.phonebook.service.exception;

/**
 * Thrown when trying to execute some actions on
 * behalf of {@code User}, but its login doesn't exist.
 * 
 * @author Yuriy Phediv
 *
 */
public class WrongLoginException extends RuntimeException{
	
	public WrongLoginException(){
		super();
	}
	
	public WrongLoginException(String msg){
		super(msg);
	}
}
