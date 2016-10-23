package ua.phonebook.service.exception;

public class DuplicateLoginException extends Exception {

	public DuplicateLoginException(){
		super();
	}
	
	public DuplicateLoginException(String msg){
		super(msg);
	}
}
