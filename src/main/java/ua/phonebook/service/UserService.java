package ua.phonebook.service;

import java.util.Set;

import org.springframework.security.access.prepost.PreAuthorize;

import ua.phonebook.model.PhoneBookRecord;
import ua.phonebook.model.User;
import ua.phonebook.service.exception.DuplicateLoginException;

public interface UserService {
	
	@PreAuthorize("isAnonymous()")
	public void registerUser(User user) throws DuplicateLoginException;
	
	
}
