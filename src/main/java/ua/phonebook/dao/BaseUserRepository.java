package ua.phonebook.dao;


import ua.phonebook.model.User;

/**
 * Base interface for saving and getting {@link User}s
 * in data storage.
 * 
 * @author Yuriy Phediv
 *
 */

public interface BaseUserRepository {
	public User getUserByLogin(String login);
	public User saveAndFlush(User user);
}
