package ua.phonebook.service.exception;

/**
 * Thrown when trying to save {@link User} with login,
 * which already exists in system.
 * 
 * @author Yuriy Phediv
 *
 */
public class DuplicateLoginException extends Exception {

	public DuplicateLoginException(){
		super();
	}
	
	public DuplicateLoginException(String msg){
		super(msg);
	}
}
