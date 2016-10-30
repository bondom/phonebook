package ua.phonebook.service;


import org.springframework.security.access.prepost.PreAuthorize;

import ua.phonebook.model.User;
import ua.phonebook.service.exception.DuplicateLoginException;

public interface UserService {
	
	/**
	 * If {@code user.login} is unique, saves {@code user} in data storage, 
	 * otherwise {@link DuplicateLoginException} is thrown.
	 * @param user {@link User}
	 * @throws DuplicateLoginException if User with the same login already exists
	 * in data storage.
	 * @return saved User
	 */
	@PreAuthorize("isAnonymous()")
	public User registerUser(User user) throws DuplicateLoginException;
	
	
}
