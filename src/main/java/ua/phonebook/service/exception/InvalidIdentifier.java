package ua.phonebook.service.exception;

public class InvalidIdentifier extends Exception {
	
	public InvalidIdentifier(){
		super();
	}
	
	public InvalidIdentifier(String msg){
		super(msg);
	}
}
